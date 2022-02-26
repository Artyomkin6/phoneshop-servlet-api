package com.es.phoneshop.model.order;

import java.util.Arrays;

public enum PaymentMethod {
    CASH("cash"),
    CREDIT_CARD("creditCard");

    private final String methodName;

    PaymentMethod(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public static PaymentMethod fromName(String name) {
        return Arrays.stream(PaymentMethod.values())
                .filter(sortField -> sortField.methodName.equals(name))
                .findAny()
                .orElse(null);
    }
}
