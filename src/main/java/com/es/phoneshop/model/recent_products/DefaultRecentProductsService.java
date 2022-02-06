package com.es.phoneshop.model.recent_products;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultRecentProductsService implements RecentProductsService {
    private Queue<Product> recentProducts;
    private static RecentProductsService instance;
    private final ReentrantLock LOCK = new ReentrantLock();
    private static final int RECENT_PRODUCTS_MAX_COUNT = 3;
    private static final String RECENT_PRODUCTS_SESSION_ATTRIBUTE
            = DefaultRecentProductsService.class.getName() + ".class";

    private DefaultRecentProductsService() {
    }

    public static RecentProductsService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DefaultRecentProductsService();
        }
        return instance;
    }

    @Override
    public Queue<Product> getRecentProducts(HttpServletRequest request) {
        synchronized (request.getSession()) {
            recentProducts = (LinkedList<Product>) request.getSession().getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE);
            if (Objects.isNull(recentProducts)) {
                recentProducts = new LinkedList<>();
                request.getSession().setAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE, recentProducts);
            }
            return recentProducts;
        }
    }

    @Override
    public void add(Queue<Product> recentProducts, Product product) {
        LOCK.lock();
        try {
            if (!isProductPresent(recentProducts, product)) {
                recentProducts.add(product);
                if (recentProducts.size() > RECENT_PRODUCTS_MAX_COUNT) {
                    recentProducts.remove();
                }
            }
        } finally {
            LOCK.unlock();
        }
    }

    private boolean isProductPresent(Queue<Product> recentProducts, Product product) {
        return recentProducts.stream()
                .anyMatch(currentProduct -> currentProduct.getId().equals(product.getId()));
    }
}
