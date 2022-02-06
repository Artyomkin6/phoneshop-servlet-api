package com.es.phoneshop.model.cart;

public class WrongQuantityException extends Exception {
    public WrongQuantityException() {
        super();
    }

    public WrongQuantityException(String message) {
        super(message);
    }

    public WrongQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongQuantityException(Throwable cause) {
        super(cause);
    }
}
