package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.cart.WrongQuantityException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_ATTRIBUTE = "cart";
    private static final String PRODUCT_IDS = "productId";
    private static final String QUANTITIES = "quantity";
    private static final String CART_LIST_PAGE_PATH
            = "/WEB-INF/pages/cart.jsp";
    private static final String ERROR_MESSAGES_ATTRIBUTE = "errors";
    private static final String ERROR_NOT_A_NUMBER = "Not a number";
    private static final String ERROR_WRONG_QUANTITY
            = "Quantity must be a positive number";
    private static final String ERROR_NOT_ENOUGH_STOCK = "Not enough stock";
    private static final String PATH_FORMAT = "%s/cart?message=%s";
    private static final String SUCCESS_MESSAGE = "Cart has been updated successfully";

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));

        request.getRequestDispatcher(CART_LIST_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues(PRODUCT_IDS);
        String[] quantities = request.getParameterValues(QUANTITIES);

        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; ++i) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            try {
                quantity = parseQuantity(quantities[i], request);
            } catch (ParseException exception) {
                errors.put(productId, ERROR_NOT_A_NUMBER);
                continue;
            }

            try {
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (WrongQuantityException exception) {
                errors.put(productId, ERROR_WRONG_QUANTITY);
            } catch (NotEnoughStockException exception) {
                errors.put(productId, ERROR_NOT_ENOUGH_STOCK);
            }
        }
        if (!errors.isEmpty()) {
            request.setAttribute(ERROR_MESSAGES_ATTRIBUTE, errors);
            doGet(request, response);
        } else {
            response.sendRedirect(
                    String.format(PATH_FORMAT, request.getContextPath(), SUCCESS_MESSAGE)
            );
        }
    }

    private int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }
}
