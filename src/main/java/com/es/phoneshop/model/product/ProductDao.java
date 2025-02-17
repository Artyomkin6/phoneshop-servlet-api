package com.es.phoneshop.model.product;

import com.es.phoneshop.model.abstract_dao.Dao;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    List<Product> findProducts();

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void delete(Long id);
}
