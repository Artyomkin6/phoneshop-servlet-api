package com.es.phoneshop.model.abstract_dao;

public class ItemNotFoundException extends RuntimeException {
    private final Long id;

    public ItemNotFoundException(Long id) {
        this.id = id;
    }

    public ItemNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    public ItemNotFoundException(Long id, String message, Throwable cause) {
        super(message, cause);
        this.id = id;
    }

    public ItemNotFoundException(Long id, Throwable cause) {
        super(cause);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
