package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.cart.WrongQuantityException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.es.phoneshop.web.AbstractProductServlet.ERROR_NOT_A_NUMBER;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_WRONG_QUANTITY;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_NOT_ENOUGH_STOCK;

public class CartPageServlet extends HttpServlet {
    private static final String CART_ATTRIBUTE = "cart";
    private static final String PRODUCT_IDS = "productId";
    private static final String QUANTITIES = "quantity";
    private static final String CART_LIST_PAGE_PATH
            = "/WEB-INF/pages/cart.jsp";
    private static final String ERRORS_SESSION_ATTRIBUTE = "cartErrors";
    private static final String QUANTITIES_SESSION_ATTRIBUTE = "errorQuantities";
    private static final String SUCCESS_PATH_FORMAT = "%s/cart?message=%s";
    private static final String SUCCESS_MESSAGE = "Cart has been updated successfully";
    private static final String ERROR_PATH_FORMAT = "%s/cart";

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request.getSession()));

        setRequestErrors(request);
        clearSessionErrors(request);

        request.getRequestDispatcher(CART_LIST_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues(PRODUCT_IDS);
        String[] quantities = request.getParameterValues(QUANTITIES);

        Map<Long, String> errors = new HashMap<>();
        Map<Long, String> quantitiesString = new HashMap<>();
        for (int i = 0; i < productIds.length; ++i) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            try {
                quantity = parseQuantity(quantities[i], request);
                cartService.update(cartService.getCart(request.getSession()), productId, quantity);
            } catch (ParseException exception) {
                handleError(errors, quantitiesString, productId, ERROR_NOT_A_NUMBER, quantities[i]);
            } catch (WrongQuantityException exception) {
                handleError(errors, quantitiesString, productId, ERROR_WRONG_QUANTITY, quantities[i]);
            } catch (NotEnoughStockException exception) {
                handleError(errors, quantitiesString, productId, ERROR_NOT_ENOUGH_STOCK, quantities[i]);
            }
        }

        setSessionMap(request.getSession(), errors, ERRORS_SESSION_ATTRIBUTE);
        setSessionMap(request.getSession(), quantitiesString, QUANTITIES_SESSION_ATTRIBUTE);

        if (errors.isEmpty()) {
            response.sendRedirect(
                    String.format(SUCCESS_PATH_FORMAT, request.getContextPath(), SUCCESS_MESSAGE)
            );
        } else {
            response.sendRedirect(
                    String.format(ERROR_PATH_FORMAT, request.getContextPath())
            );
        }
    }

    private int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }

    private void handleError(Map<Long, String> errors, Map<Long, String> quantitiesString,
                             Long productId, String errorMessage, String quantityString) {
        errors.put(productId, errorMessage);
        quantitiesString.put(productId, quantityString);
    }

    private void setRequestErrors(HttpServletRequest request) {
        Map<Long, String> errors = getSessionMap(request.getSession(), ERRORS_SESSION_ATTRIBUTE);
        Map<Long, String> quantitiesString = getSessionMap(request.getSession(), QUANTITIES_SESSION_ATTRIBUTE);
        request.setAttribute(ERRORS_SESSION_ATTRIBUTE, errors);
        request.setAttribute(QUANTITIES_SESSION_ATTRIBUTE, quantitiesString);
    }

    private void clearSessionErrors(HttpServletRequest request) {
        setSessionMap(request.getSession(), new HashMap<>(), ERRORS_SESSION_ATTRIBUTE);
        setSessionMap(request.getSession(), new HashMap<>(), QUANTITIES_SESSION_ATTRIBUTE);
    }

    private Map<Long, String> getSessionMap(HttpSession session, String mapAttribute) {
        synchronized (session) {
            Map<Long, String> map =
                    (Map<Long, String>) session.getAttribute(mapAttribute);
            if (map == null) {
                map = new HashMap<>();
            }
            return map;
        }
    }

    private void setSessionMap(HttpSession session, Map<Long, String> map, String mapAttribute) {
        synchronized (session) {
            if (map == null) {
                map = new HashMap<>();
            }
            session.setAttribute(mapAttribute, map);
        }
    }
}
