package com.es.phoneshop.model.product;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class ProductSearchComparator implements Comparator<Product> {
    private String query;

    public ProductSearchComparator(String query) {
        if (query != null) {
            this.query = query.trim().toLowerCase(Locale.ROOT);
        } else {
            this.query = null;
        }
    }

    private long countMatches(Product product) {
        return Arrays.stream(product.getDescription()
                        .toLowerCase(Locale.ROOT)
                        .split(" "))
                .filter(part -> query.contains(part))
                .count();
    }

    @Override
    public int compare(Product product1, Product product2) {
        if ((query == null) || query.isEmpty()) {
            return 0;
        }
        long matches1 = countMatches(product1);
        long matches2 = countMatches(product2);
        int matchesCompare = Long.compare(matches2, matches1);
        if (matchesCompare != 0) {
            return matchesCompare;
        } else {
            return product1.getDescription()
                    .compareToIgnoreCase(product2.getDescription());
        }
    }
}
