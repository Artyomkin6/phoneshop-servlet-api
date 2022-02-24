package com.es.phoneshop.model.abstract_dao;

public interface Dao<T extends ItemWithId> {
    T getById(Long id) throws ItemNotFoundException;

    void save(T item);
}
