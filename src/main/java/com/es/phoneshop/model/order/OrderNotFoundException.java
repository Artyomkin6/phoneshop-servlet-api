package com.es.phoneshop.model.order;

import com.es.phoneshop.model.abstract_dao.ItemNotFoundException;

public class OrderNotFoundException extends ItemNotFoundException {
    private final String secureId;

    public OrderNotFoundException(String secureId) {
        super(null);
        this.secureId = secureId;
    }

    public OrderNotFoundException(Long orderId) {
        super(orderId);
        secureId = null;
    }

    public OrderNotFoundException(String secureId, String message) {
        super(null, message);
        this.secureId = secureId;
    }

    public OrderNotFoundException(Long orderId, String message) {
        super(orderId, message);
        secureId = null;
    }

    public OrderNotFoundException(String secureId, String message, Throwable cause) {
        super(null, message, cause);
        this.secureId = secureId;
    }

    public OrderNotFoundException(Long orderId, String message, Throwable cause) {
        super(orderId, message, cause);
        secureId = null;
    }

    public OrderNotFoundException(String secureId, Throwable cause) {
        super(null, cause);
        this.secureId = secureId;
    }

    public OrderNotFoundException(Long orderId, Throwable cause) {
        super(orderId, cause);
        secureId = null;
    }

    public String getSecureId() {
        return secureId;
    }
}
