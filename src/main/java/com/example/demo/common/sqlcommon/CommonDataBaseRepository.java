package com.example.demo.common.sqlcommon;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Repository
@Slf4j
public class CommonDataBaseRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDataBaseRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CommonDataBaseRepository() {
    }

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(1000);
        return namedParameterJdbcTemplate;
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<T> findAll(Class<T> persistentClass) {
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t";
        return this.entityManager.createQuery(sqlQuery).getResultList();
    }

    public List<T> findByMultilParam(Class<T> persistentClass, Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i % 2 == 0) {
                    sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                    mapParams.put("p_" + params[i], params[i + 1]);
                }
            }
        }
        Query query = this.entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    public int deleteByMultilParam(Class<T> persistentClass, Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " Delete from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i % 2 == 0) {
                    sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                    mapParams.put("p_" + params[i], params[i + 1]);
                }
            }
        }
        Query query = this.entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    /**
     * Check unique with singe field
     */
    public boolean checkUnique(Class<T> persistentClass, String uniqueField, Object uniqueValue,
                               String idField, Long idValue) {
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (uniqueValue instanceof String) {
            sqlQuery += " AND lower(t." + uniqueField + ") = :p_" + uniqueField + " ";
        } else {
            sqlQuery += " AND t." + uniqueField + " = :p_" + uniqueField + " ";
        }
        sqlQuery += " AND t." + idField + " <> :p_" + idField;
        Query query = this.entityManager.createQuery(sqlQuery);
        if (uniqueValue instanceof String) {
            query.setParameter("p_" + uniqueField, uniqueValue.toString().trim().toLowerCase());
        } else {
            query.setParameter("p_" + uniqueField, uniqueValue);
        }
        query.setParameter("p_" + idField, idValue);
        List<T> lst = query.getResultList();
        return lst == null || lst.size() == 0;
    }

    /**
     * Check unique with multiple field
     */
    public boolean checkUniqueWithMultiFields(Class<T> persistentClass, String idField, Long idValue,
                                              Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i % 2 == 0) {
                    if (params[i + 1] != null) {
                        boolean isNumber = false;
                        try {
                            Field field = persistentClass.getDeclaredField(String.valueOf(params[i]));
                            String returnType = field.getType().getSimpleName().toUpperCase();
                            if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
                                isNumber = true;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        if (params[i + 1] instanceof String && !isNumber) {
                            sqlQuery += " AND lower(t." + params[i] + ") = :p_" + params[i] + " ";
                            mapParams.put("p_" + params[i], params[i + 1].toString().trim().toLowerCase());
                        } else {
                            sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                            mapParams.put("p_" + params[i], params[i + 1]);
                        }
                    }
                }
            }
        }
        sqlQuery += " AND t." + idField + " <> :p_" + idField;
        Query query = this.entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            try {
                String fieldName = entry.getKey().split("_")[1];
                Field field = persistentClass.getDeclaredField(fieldName);
                String returnType = field.getType().getSimpleName().toUpperCase();
                if ("Double".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Double.valueOf(String.valueOf(entry.getValue())));
                } else if ("Integer".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue())));
                } else if ("Float".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Float.valueOf(String.valueOf(entry.getValue())));
                } else if ("Short".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Short.valueOf(String.valueOf(entry.getValue())));
                } else if ("Long".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
                continue;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("p_" + idField, idValue);
        List<T> lst = query.getResultList();
        return lst == null || lst.size() == 0;
    }

    /**
     * Check unique with multiple field has field null
     */
    public boolean checkUniqueWithMultiFieldsHasNull(Class<T> persistentClass, String idField,
                                                     Long idValue, Map<String, Object> mapFields, Object... params) {
        Map<String, Object> mapParams = new HashMap<>();
        String sqlQuery = " Select t from " + persistentClass.getSimpleName() + " t WHERE 1=1 ";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                boolean isNumber = false;
                try {
                    Field field = persistentClass.getDeclaredField(String.valueOf(params[i]));
                    String returnType = field.getType().getSimpleName().toUpperCase();
                    if (Constants.TYPE_NUMBER.indexOf(returnType) >= 0) {
                        isNumber = true;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (mapFields.get(params[i]) != null) {
                    if (mapFields.get(params[i]) instanceof String && !isNumber) {
                        sqlQuery += " AND lower(t." + params[i] + ") = :p_" + params[i] + " ";
                        mapParams
                                .put("p_" + params[i], mapFields.get(params[i]).toString().trim().toLowerCase());
                    } else {
                        sqlQuery += " AND t." + params[i] + " = :p_" + params[i] + " ";
                        mapParams.put("p_" + params[i], mapFields.get(params[i]));
                    }
                } else {
                    sqlQuery += " AND t." + params[i] + " is null";
                }
            }
        }
        sqlQuery += " AND t." + idField + " <> :p_" + idField;
        Query query = this.entityManager.createQuery(sqlQuery);
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            try {
                String fieldName = entry.getKey().split("_")[1];
                Field field = persistentClass.getDeclaredField(fieldName);
                String returnType = field.getType().getSimpleName().toUpperCase();
                if ("Double".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Double.valueOf(String.valueOf(entry.getValue())));
                } else if ("Integer".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue())));
                } else if ("Float".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Float.valueOf(String.valueOf(entry.getValue())));
                } else if ("Short".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Short.valueOf(String.valueOf(entry.getValue())));
                } else if ("Long".equalsIgnoreCase(returnType)) {
                    query.setParameter(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue())));
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
                continue;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setParameter("p_" + idField, idValue);
        List<T> lst = query.getResultList();
        return lst == null || lst.size() == 0;
    }

    public Object getFirstData(StringBuilder queryString, List<Object> arrParams, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        int countParams = 1;
        if (arrParams != null) {
            for (Iterator var6 = arrParams.iterator(); var6.hasNext(); ++countParams) {
                Object arrParam = var6.next();
                query.setParameter(countParams, arrParam);
            }
        }
        query.setFirstResult(0).setMaxResults(1);
        List objectList = query.getResultList();
        List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
        this.entityManager.close();
        return listResult;
    }

    public Object getFirstData(StringBuilder queryString, HashMap<String, Object> hmapParams, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        if (hmapParams != null) {
            Set set = hmapParams.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Entry mentry = (Entry) iterator.next();
                query.setParameter(mentry.getKey().toString(), mentry.getValue());
            }
        }
        query.setFirstResult(0).setMaxResults(1);
        List objectList = query.getResultList();
        List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
        this.entityManager.close();
        return listResult != null && listResult.size() > 0 ? listResult.get(0) : null;
    }

    public List<? extends Object> getListData(StringBuilder queryString, List<Object> arrParams, Integer startPage, Integer pageLoad, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        int countParams = 1;
        if (arrParams != null) {
            for (Iterator var8 = arrParams.iterator(); var8.hasNext(); ++countParams) {
                Object arrParam = var8.next();
                query.setParameter(countParams, arrParam);
            }
        }
        if (startPage != null && pageLoad != null) {
            query.setFirstResult(startPage).setMaxResults(pageLoad);
        }
        List objectList = query.getResultList();
        List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
        this.entityManager.close();
        return listResult;
    }

    public List<? extends Object> getListData(StringBuilder queryString, HashMap<String, Object> hmapParams, Class<?> classOfT) {
        return this.getListData(queryString, hmapParams, null, null, classOfT);
    }

    public List<? extends Object> getListData(StringBuilder queryString, HashMap<String, Object> hmapParams, Integer startPage, Integer pageLoad, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        if (hmapParams != null) {
            Set set = hmapParams.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Entry mentry = (Entry) iterator.next();
                query.setParameter(mentry.getKey().toString(), mentry.getValue());
            }
        }
        if (startPage != null && pageLoad != null) {
            query.setFirstResult(startPage).setMaxResults(pageLoad);
        }
        List objectList = query.getResultList();
        List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
        this.entityManager.close();
        return listResult;
    }

    public ResultSelectEntity getListDataAndCount(StringBuilder queryString, HashMap<String, Object> hmapParams, Class<?> classOfT) {
        return this.getListDataAndCount(queryString, hmapParams, null, null, classOfT);
    }

    public ResultSelectEntity getListDataAndCount(StringBuilder queryString, HashMap<String, Object> hmapParams, Integer startPage, Integer pageLoad, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        if (hmapParams != null) {
            Set set = hmapParams.entrySet();
            Entry mentry;
            Object value;
            for (Iterator iterator = set.iterator(); iterator.hasNext(); query.setParameter(mentry.getKey().toString(), value)) {
                mentry = (Entry) iterator.next();
                value = mentry.getValue();
                if (value == null) {
                    value = "";
                }
            }
        }
        Object cResults = query.getResultList().size();
        if (startPage != null && pageLoad != null) {
            query.setFirstResult(startPage).setMaxResults(pageLoad);
        }
        List objectList = query.getResultList();
        ResultSelectEntity result = new ResultSelectEntity();
        if (objectList != null) {
            List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
            if (listResult != null) {
                result.setListData(listResult);
            }
        }
        result.setCount(cResults);
        this.entityManager.close();
        return result;
    }

    public ResultSelectEntity getListDataAndCount(StringBuilder queryString, List<Object> arrParams, Integer startPage, Integer pageLoad, Class<?> classOfT) {
        Query query = this.entityManager.createNativeQuery(queryString.toString(), Tuple.class);
        int countParams = 1;
        if (arrParams != null) {
            for (Iterator var8 = arrParams.iterator(); var8.hasNext(); ++countParams) {
                Object arrParam = var8.next();
                query.setParameter(countParams, arrParam);
            }
        }
        Object cResults = query.getResultList().size();
        if (startPage != null && pageLoad != null) {
            query.setFirstResult(startPage).setMaxResults(pageLoad);
        }
        List objectList = query.getResultList();
        ResultSelectEntity result = new ResultSelectEntity();
        if (objectList != null) {
            List<Object> listResult = FnCommon.convertToEntity(objectList, classOfT);
            if (listResult != null) {
                result.setListData(listResult);
            }
        }
        result.setCount(cResults);
        this.entityManager.close();
        return result;
    }

    public int getCountData(StringBuilder queryString, List<Object> arrParams) {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("Select count(*) as count From (");
        strBuild.append(queryString);
        strBuild.append(")");
        Query query = this.entityManager.createNativeQuery(strBuild.toString());
        int countParams = 1;
        if (arrParams != null) {
            for (Iterator var6 = arrParams.iterator(); var6.hasNext(); ++countParams) {
                Object arrParam = var6.next();
                query.setParameter(countParams, arrParam);
            }
        }
        List<BigDecimal> resultList = query.getResultList();
        BigDecimal result = resultList.get(0);
        this.entityManager.close();
        return result.intValue();
    }

    public Long getNumberData(StringBuilder queryString, HashMap<String, Object> hmapParams) {
        Query query = this.entityManager.createNativeQuery(queryString.toString());

        if (hmapParams != null) {
            Set set = hmapParams.entrySet();
            Entry mentry;
            Object value;
            for (Iterator iterator = set.iterator(); iterator.hasNext(); query.setParameter(mentry.getKey().toString(), value)) {
                mentry = (Entry) iterator.next();
                value = mentry.getValue();
                if (value == null) {
                    value = "";
                }
            }
        }
        Object result = query.getSingleResult();
        this.entityManager.close();

        if (result == null) {
            return 0L;
        }

        return Long.valueOf(String.valueOf(result));
    }

    public Boolean excuteSqlDatabase(StringBuilder queryString, List<Object> arrParams) {
        Boolean result = true;
        try {
            this.entityManager.getTransaction().begin();
            Query query = this.entityManager.createNativeQuery(queryString.toString());
            int countParams = 1;
            if (arrParams != null && arrParams.size() > 0) {
                for (Iterator var6 = arrParams.iterator(); var6.hasNext(); ++countParams) {
                    Object arrParam = var6.next();
                    query.setParameter(countParams, arrParam);
                }
            }
            int resultInt = query.executeUpdate();
            LOGGER.info("resultInt= " + resultInt);
            this.entityManager.getTransaction().commit();
        } catch (Exception var8) {
            LOGGER.error(var8.getMessage(), var8);
            this.entityManager.getTransaction().rollback();
            result = false;
        }
        this.entityManager.close();
        return result;
    }

    public Boolean excuteSqlDatabase(StringBuilder queryString, HashMap<String, Object> hmapParams) {
        boolean result = true;
        try {
            Query query = this.entityManager.createNativeQuery(queryString.toString());
            if (hmapParams != null) {
                Set set = hmapParams.entrySet();
                Entry mentry;
                Object value;
                for (Iterator iterator = set.iterator(); iterator.hasNext(); query.setParameter(mentry.getKey().toString(), value)) {
                    mentry = (Entry) iterator.next();
                    value = mentry.getValue();
                    if (value == null) {
                        value = "";
                    }
                }
            }
            int resultInt = query.executeUpdate();
            LOGGER.info("resultInt= " + resultInt);
        } catch (Exception var9) {
            LOGGER.error(var9.getMessage(), var9);
            result = false;
        }
        this.entityManager.close();
        return result;
    }

    public Boolean executeAndRollbackAllWhenError(List<SqlEntity> listSqlExcuteOrRollback) {
        if (listSqlExcuteOrRollback != null && listSqlExcuteOrRollback.size() > 0) {
            Boolean result = true;
            try {
                this.entityManager.getTransaction().begin();
                Iterator var3 = listSqlExcuteOrRollback.iterator();
                while (var3.hasNext()) {
                    SqlEntity sqlEntity = (SqlEntity) var3.next();
                    String queryString = sqlEntity.getSql();
                    List<Object> arrParams = sqlEntity.getListParams();
                    Query query = this.entityManager.createNativeQuery(queryString);
                    int countParams = 1;
                    if (arrParams != null && arrParams.size() > 0) {
                        for (Iterator var9 = arrParams.iterator(); var9.hasNext(); ++countParams) {
                            Object arrParam = var9.next();
                            query.setParameter(countParams, arrParam);
                        }
                    }
                    int resultInt = query.executeUpdate();
                    LOGGER.info("resultInt= " + resultInt);
                }
                this.entityManager.getTransaction().commit();
            } catch (Exception var11) {
                LOGGER.error(var11.getMessage(), var11);
                this.entityManager.getTransaction().rollback();
            }
            this.entityManager.close();
            return result;
        } else {
            return false;
        }
    }

    public String fullNameColHandle(String firstNameCol, String lastNameCol, String alias) {
        return " TRIM(CONCAT(COALESCE(" + firstNameCol + ", ''), ' ', COALESCE(" + lastNameCol + ", ''))) AS " + alias + ", ";
    }
}
