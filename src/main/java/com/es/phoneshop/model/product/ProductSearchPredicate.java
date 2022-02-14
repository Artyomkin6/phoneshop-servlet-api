package com.es.phoneshop.model.product;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;

public class ProductSearchPredicate implements Predicate<Product> {
    private String query;

    public ProductSearchPredicate(String query) {
        if (query != null) {
            this.query = query.trim().toLowerCase(Locale.ROOT);
        } else {
            this.query = "";
        }
    }

    @Override
    public boolean test(Product product) {
        return Arrays.stream(query.split(" "))
                .anyMatch(part -> product.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .contains(part));
    }
}
