package com.es.phoneshop.model.recent_products;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
import java.util.Queue;

public interface RecentProductsService {
    Queue<Product> getRecentProducts(HttpSession session);

    void addRecentProduct(Queue<Product> recentProducts, Product product);
}
