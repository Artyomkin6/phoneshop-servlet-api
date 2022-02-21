package com.es.phoneshop.model.product;

import com.es.phoneshop.model.abstract_dao.ItemNotFoundException;

public class ProductNotFoundException extends ItemNotFoundException {
    public ProductNotFoundException(Long productId) {
        super(productId);
    }

    public ProductNotFoundException(Long productId, String message) {
        super(productId, message);
    }

    public ProductNotFoundException(Long productId, String message, Throwable cause) {
        super(productId, message, cause);
    }

    public ProductNotFoundException(Long productId, Throwable cause) {
        super(productId, cause);
    }
}
