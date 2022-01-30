package com.es.phoneshop.model.product;

import java.util.Arrays;

public enum SortOrder {
    ASCENDING("ascending"),
    DESCENDING("descending");

    private String fieldName;

    SortOrder(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static SortOrder fromName(String name) {
        return Arrays.stream(SortOrder.values())
                .filter(sortOrder -> sortOrder.fieldName.equals(name))
                .findAny()
                .orElse(null);
    }
}
