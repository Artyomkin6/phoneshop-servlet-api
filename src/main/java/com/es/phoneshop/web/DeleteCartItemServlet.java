package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private static final String PATH_FORMAT
            = "%s/cart?message=Cart item removed successfully";

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productId = parseProductId(request);
        deleteProductFromCart(request, productId);
        response.sendRedirect(String.format(PATH_FORMAT, request.getContextPath()));
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }

    private void deleteProductFromCart(HttpServletRequest request, Long productId) {
        Cart cart = cartService.getCart(request);
        cartService.delete(cart, productId);
    }
}
