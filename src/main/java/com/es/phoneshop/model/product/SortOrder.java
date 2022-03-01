package com.es.phoneshop.model.product;

import java.util.Arrays;

public enum SortOrder {
    ASCENDING("ascending"),
    DESCENDING("descending");

    private String sortOrderName;

    SortOrder(String sortOrderName) {
        this.sortOrderName = sortOrderName;
    }

    public String getSortOrderName() {
        return sortOrderName;
    }

    public static SortOrder fromName(String name) {
        return Arrays.stream(SortOrder.values())
                .filter(sortOrder -> sortOrder.sortOrderName.equals(name))
                .findAny()
                .orElse(null);
    }
}
