package com.es.phoneshop.model.product;

import java.util.Arrays;

public enum SortField {
    DESCRIPTION("description"),
    PRICE("price");

    private String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static SortField fromName(String name) {
        return Arrays.stream(SortField.values())
                .filter(sortField -> sortField.fieldName.equals(name))
                .findAny()
                .orElse(null);
    }
}
