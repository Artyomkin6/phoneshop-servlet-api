package com.es.phoneshop.model.order;

import java.util.Arrays;

public enum PaymentMethod {
    CASH("cash"),
    CREDIT_CARD("creditCard");

    private String fieldName;

    PaymentMethod(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static PaymentMethod fromName(String name) {
        return Arrays.stream(PaymentMethod.values())
                .filter(sortField -> sortField.fieldName.equals(name))
                .findAny()
                .orElse(null);
    }
}
