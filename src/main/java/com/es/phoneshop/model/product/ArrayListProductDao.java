package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private long nextId = 1;
    private static ArrayListProductDao instance;
    private final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    public static synchronized ArrayListProductDao getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        LOCK.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        return findProducts("", null, null);
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        LOCK.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> !(product.getPrice().equals(BigDecimal.ZERO)))
                    .filter(product -> product.getStock() > 0)
                    .filter(new ProductSearchPredicate(query))
                    .sorted(new ProductSearchComparator(query))
                    .sorted(new ProductSortComparator(sortField, sortOrder))
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

    private void addWithValidation(Product product) throws InvalidProductException {
        if (ProductValidator.validateProduct(product)) {
            products.add(product);
        } else {
            throw new InvalidProductException();
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

    public void deleteAll() {
        LOCK.writeLock().lock();
        try {
            products.clear();
            nextId = 1;
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
