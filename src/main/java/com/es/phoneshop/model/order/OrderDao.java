package com.es.phoneshop.model.order;

import com.es.phoneshop.model.abstract_dao.Dao;

public interface OrderDao extends Dao<Order> {
    Order getOrderBySecureId(String secureId) throws OrderNotFoundException;
}
