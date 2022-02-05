package com.es.phoneshop.model.product;

import java.util.Comparator;

public class ProductSortComparator implements Comparator<Product> {
    private SortField sortField;
    private SortOrder sortOrder;

    public ProductSortComparator(SortField sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Product product1, Product product2) {
        int compareResult;
        if (SortField.DESCRIPTION == sortField) {
            compareResult = product1.getDescription()
                    .compareToIgnoreCase(product2.getDescription());
        } else if (SortField.PRICE == sortField) {
            compareResult = product1.getPrice().compareTo(product2.getPrice());
        } else {
            return 0;
        }
        if (SortOrder.ASCENDING == sortOrder) {
            return compareResult;
        } else if (SortOrder.DESCENDING == sortOrder) {
            return -compareResult;
        } else {
            return 0;
        }
    }
}
