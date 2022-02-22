package com.es.phoneshop.model.recent_products;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultRecentProductsService implements RecentProductsService {
    private static final int RECENT_PRODUCTS_MAX_COUNT = 3;
    private static final String RECENT_PRODUCTS_SESSION_ATTRIBUTE
            = DefaultRecentProductsService.class.getName() + ".class";
    private final ReentrantLock LOCK = new ReentrantLock();

    private static RecentProductsService instance;

    private DefaultRecentProductsService() {
    }

    public static synchronized RecentProductsService getInstance() {
        if (instance == null) {
            instance = new DefaultRecentProductsService();
        }
        return instance;
    }

    @Override
    public Queue<Product> getRecentProducts(HttpSession session) {
        synchronized (session) {
            Queue<Product> recentProducts
                    = (LinkedList<Product>) session.getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE);
            if (recentProducts == null) {
                recentProducts = new LinkedList<>();
                session.setAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE, recentProducts);
            }
            return recentProducts;
        }
    }

    @Override
    public void addRecentProduct(Queue<Product> recentProducts, Product product) {
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
                .anyMatch(currentProduct -> currentProduct.equals(product));
    }
}
