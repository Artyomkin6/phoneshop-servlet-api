package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    private Long productId;

    public ProductNotFoundException(Long productId) {
        this.productId = productId;
    }

    public ProductNotFoundException(Long productId, String message) {
        super(message);
        this.productId = productId;
    }

    public ProductNotFoundException(Long productId, String message, Throwable cause) {
        super(message, cause);
        this.productId = productId;
    }

    public ProductNotFoundException(Long productId, Throwable cause) {
        super(cause);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
