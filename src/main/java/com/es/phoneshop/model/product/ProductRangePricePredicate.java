package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class ProductRangePricePredicate implements Predicate<Product> {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public ProductRangePricePredicate(BigDecimal minPrice, BigDecimal maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean test(Product product) {
        return (minPrice == null || (product.getPrice().compareTo(minPrice) >= 0))
                && (maxPrice == null || (product.getPrice().compareTo(maxPrice) <= 0));
    }
}
