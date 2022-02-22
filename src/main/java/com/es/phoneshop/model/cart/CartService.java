package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException, WrongQuantityException;

    void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException, WrongQuantityException;

    void delete(Cart cart, Long productId);
}
