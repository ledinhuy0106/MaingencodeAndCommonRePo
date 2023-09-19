package com.example.demo.repository.repositoryimpl;

import com.example.demo.common.sqlcommon.CommonDataBaseRepository;
import com.example.demo.entity.dto.ProductDTO;
import com.example.demo.entity.model.Product;
import com.example.demo.repository.ProductRepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ProductRepositoryImpl extends CommonDataBaseRepository implements ProductRepositoryService {
    public Iterable<Product> getAll(ProductDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" p.product_id as id, ");
        sql.append(" p.product_name as name, ");
        sql.append(" p.amount as amount, ");
        sql.append(" p.price as price, ");
        sql.append(" p.category_id as categoryId, ");
        sql.append(" ca.name as categoryName ");
        sql.append(" FROM product p join Category ca on p.category_id= ca.category_id  ");
        sql.append(" where 1=1 ");
        HashMap<String, Object> hmapParams = new HashMap<>();

        if (itemParamsEntity.getName() != null) {
            sql.append(" AND lower(p.product_name) like :name ");
            hmapParams.put("name", "%" + itemParamsEntity.getName().trim().toLowerCase() + "%"); // Thêm ký tự '%' để tìm kiếm theo phần của tên sản phẩm
        }

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


        return (List<Product>) getListData(sql,hmapParams,start,pageSize,ProductDTO.class);
    }

}
