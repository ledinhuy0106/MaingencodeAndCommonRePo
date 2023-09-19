package com.example.demo.repository.repositoryimpl;

import com.example.demo.common.sqlcommon.CommonDataBaseRepository;
import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Category;
import com.example.demo.repository.CategoryRepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Repository
public class CategoryRepositoryImpl extends CommonDataBaseRepository implements CategoryRepositoryService {
    public Iterable<Category> getAll(ProductDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * from Category");
        sql.append(" where 1=1 ");
        HashMap<String, Object> hmapParams = new HashMap<>();
        if (StringUtils.isNotBlank(itemParamsEntity.getCategoryName())) {
            sql.append(" AND lower(ca.name) like :categoryName ");
            hmapParams.put("categoryName", "%" + itemParamsEntity.getCategoryName() + "%"); // Thêm ký tự '%' để tìm kiếm theo phần của tên danh mục
        }

        Integer start = null;
        if (itemParamsEntity.getPage() != null) {
            start = itemParamsEntity.getPage();
        }
        Integer pageSize = null;
        if (itemParamsEntity.getSize() != null) {
            pageSize = itemParamsEntity.getSize();
        }


        return (List<Category>) getListData(sql,hmapParams,start,pageSize,ProductDTO.class);
    }
}
