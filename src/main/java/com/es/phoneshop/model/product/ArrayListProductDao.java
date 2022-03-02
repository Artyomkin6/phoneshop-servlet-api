package com.es.phoneshop.model.product;

import com.es.phoneshop.model.abstract_dao.AbstractArrayListDao;
import com.es.phoneshop.model.abstract_dao.ItemNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractArrayListDao<Product> implements ProductDao {
    private static ArrayListProductDao instance;

    private ArrayListProductDao() {
        super();
    }

    public static synchronized ArrayListProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getById(Long id) throws ProductNotFoundException {
        try {
            return super.getById(id);
        } catch (ItemNotFoundException exception) {
            throw new ProductNotFoundException(exception.getId());
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
            return items.stream()
                    .filter(product -> !(BigDecimal.ZERO.equals(product.getPrice())))
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
    public List<Product> searchProducts(String productCode, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock) {
        LOCK.readLock().lock();
        try {
            List<Product> validProducts = findProducts();
            return validProducts.stream()
                    .filter(new ProductCodePredicate(productCode))
                    .filter(new ProductRangePricePredicate(minPrice, maxPrice))
                    .filter(new ProductStockPredicate(minStock))
                    .collect(Collectors.toList());
        } finally {
            LOCK.readLock().unlock();
        }
    }

    protected void add(Product product) throws InvalidProductException {
        if (ProductValidator.validateProduct(product)) {
            items.add(product);
        } else {
            throw new InvalidProductException();
        }
    }

    @Override
    public void delete(Long id) {
        LOCK.writeLock().lock();
        try {
            items.removeIf(product -> id.equals(product.getId()));
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    public void deleteAll() {
        LOCK.writeLock().lock();
        try {
            items.clear();
            nextId = 1;
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
