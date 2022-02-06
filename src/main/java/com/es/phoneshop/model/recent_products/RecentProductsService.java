package com.es.phoneshop.model.recent_products;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Queue;

public interface RecentProductsService {
    Queue<Product> getRecentProducts(HttpServletRequest request);

    void add(Queue<Product> recentProducts, Product product);
}
