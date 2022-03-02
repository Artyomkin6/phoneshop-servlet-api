package com.es.phoneshop.model.product;

import java.util.Locale;
import java.util.function.Predicate;

public class ProductCodePredicate implements Predicate<Product> {
    private String productCode;

    public ProductCodePredicate(String productCode) {
        if (productCode != null) {
            this.productCode = productCode.trim().toLowerCase(Locale.ROOT);
        } else {
            this.productCode = "";
        }
    }

    @Override
    public boolean test(Product product) {
        if (!productCode.equals("")) {
            return product.getCode().contains(productCode);
        }
        return true;
    }
}
