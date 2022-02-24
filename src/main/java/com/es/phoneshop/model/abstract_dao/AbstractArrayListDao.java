package com.es.phoneshop.model.abstract_dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractArrayListDao<T extends ItemWithId> implements Dao<T> {
    protected final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    protected long nextId = 1;
    protected List<T> items;

    protected AbstractArrayListDao() {
        items = new ArrayList<>();
    }

    @Override
    public T getById(Long id) throws ItemNotFoundException {
        LOCK.readLock().lock();
        try {
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny()
                    .orElseThrow(() -> new ItemNotFoundException(id));
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public void save(T newItem) {
        LOCK.writeLock().lock();
        try {
            if (newItem.getId() != null) {
                boolean exists = items.stream()
                        .anyMatch(product -> newItem.getId().equals(product.getId()));
                if (!exists) {
                    add(newItem);
                }
            } else {
                newItem.setId(nextId++);
                add(newItem);
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    protected abstract void add(T item);
}
