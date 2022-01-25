package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.ReadWriteLock;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private long nextId = 1;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    public ArrayListProductDao() {
        products = new ArrayList<>();
    }

    private void addWithValidation(Product product) throws InvalidProductException {
        if (ProductValidator.validateProduct(product)) {
            products.add(product);
        } else {
            throw new InvalidProductException();
        }
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        LOCK.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .get();
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        LOCK.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> !(product.getPrice().equals(BigDecimal.ZERO)))
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public void save(Product newProduct) throws InvalidProductException {
        LOCK.writeLock().lock();
        try {
            if (newProduct.getId() != null) {
                boolean exists = products.stream()
                        .anyMatch(product -> newProduct.getId().equals(product.getId()));
                if (!exists) {
                    addWithValidation(newProduct);
                }
            } else {
                newProduct.setId(nextId++);
                addWithValidation(newProduct);
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        LOCK.writeLock().lock();
        try {
            products.removeIf(product -> id.equals(product.getId()));
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
