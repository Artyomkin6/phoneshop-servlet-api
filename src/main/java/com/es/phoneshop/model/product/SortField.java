package com.es.phoneshop.model.product;

import java.util.Arrays;

public enum SortField {
    DESCRIPTION("description"),
    PRICE("price");

    private String sortFieldName;

    SortField(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public static SortField fromName(String name) {
        return Arrays.stream(SortField.values())
                .filter(sortField -> sortField.sortFieldName.equals(name))
                .findAny()
                .orElse(null);
    }
}
