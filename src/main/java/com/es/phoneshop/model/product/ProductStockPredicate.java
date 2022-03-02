package com.es.phoneshop.model.product;

import java.util.function.Predicate;

public class ProductStockPredicate implements Predicate<Product> {
    private Integer stock;

    public ProductStockPredicate(Integer stock) {
        this.stock = stock;
    }

    @Override
    public boolean test(Product product) {
        return (stock == null || (product.getStock()) > stock);
    }
}
