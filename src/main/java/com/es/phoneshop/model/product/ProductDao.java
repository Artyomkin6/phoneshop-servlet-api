package com.es.phoneshop.model.product;

import com.es.phoneshop.model.abstract_dao.Dao;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts();

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> searchProducts(String productCode, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock);

    void delete(Long id);
}
