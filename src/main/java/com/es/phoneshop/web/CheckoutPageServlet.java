package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.web.helpers.InputValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CheckoutPageServlet extends HttpServlet {
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String FIRST_NAME_PARAMETER = "firstName";
    private static final String LAST_NAME_PARAMETER = "lastName";
    private static final String PHONE_PARAMETER = "phone";
    private static final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    private static final String ADDRESS_PARAMETER = "address";
    private static final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    private static final String ERROR_MESSAGE = "Value is required";
    private static final String ERROR_WRONG_DATE = "Wrong date format";
    private static final String ERRORS_SESSION_ATTRIBUTE = "checkoutErrors";
    private static final String OLD_VALUES_SESSION_ATTRIBUTE = "oldValues";
    private static final String CHECKOUT_PAGE_PATH
            = "/WEB-INF/pages/checkout.jsp";
    private static final String SUCCESS_PATH_FORMAT = "%s/order/overview/%s";
    private static final String ERROR_PATH_FORMAT = "%s/checkout";
    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    private static final InputValidator validator = new InputValidator();

    private CartService cartService;
    private OrderService orderService;
    private Map<String, String> errors;
    private Map<String, String> oldValues;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute(ORDER_ATTRIBUTE, orderService.getOrder(cart));

        setRequestErrors(request);
        clearSessionErrors(request);

        request.getRequestDispatcher(CHECKOUT_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        errors = new HashMap<>();
        oldValues = new HashMap<>();

        setRequiredParameter(request, FIRST_NAME_PARAMETER, order::setFirstName, validator::validateName);
        setRequiredParameter(request, LAST_NAME_PARAMETER, order::setLastName, validator::validateName);
        setRequiredParameter(request, PHONE_PARAMETER, order::setPhone, validator::validatePhone);
        setRequiredParameter(request, ADDRESS_PARAMETER, order::setDeliveryAddress, input -> true);
        setOrderDeliveryDate(request, order, validator::validateDate);
        setPaymentMethod(request, order);

        setSessionMap(request.getSession(), errors, ERRORS_SESSION_ATTRIBUTE);
        setSessionMap(request.getSession(), oldValues, OLD_VALUES_SESSION_ATTRIBUTE);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clearCart(request.getSession());
            response.sendRedirect(
                    String.format(SUCCESS_PATH_FORMAT, request.getContextPath(), order.getSecureId())
            );
        } else {
            response.sendRedirect(
                    String.format(ERROR_PATH_FORMAT, request.getContextPath())
            );
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Consumer<String> orderSetter,
                                      Function<String, Boolean> checkInput) {
        String value = request.getParameter(parameter);
        oldValues.put(parameter, value);
        boolean isValid = checkInput.apply(value);
        if (value == null || value.isEmpty() || (!isValid)) {
            errors.put(parameter, ERROR_MESSAGE);
        } else {
            orderSetter.accept(value);
        }
    }

    private void setOrderDeliveryDate(HttpServletRequest request, Order order, Function<String, Boolean> checkInput) {
        try {
            LocalDate deliveryDate = parseDate(request, oldValues, checkInput);
            order.setDeliveryDate(deliveryDate);
        } catch (DateTimeParseException exception) {
            errors.put(DELIVERY_DATE_PARAMETER, ERROR_WRONG_DATE);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Order order) {
        String paymentMethodString = request.getParameter(PAYMENT_METHOD_PARAMETER);
        order.setPaymentMethod(PaymentMethod.fromName(paymentMethodString));
    }

    private LocalDate parseDate(HttpServletRequest request, Map<String, String> oldValues,
                                Function<String, Boolean> checkInput) {
        String dateString = request.getParameter(DELIVERY_DATE_PARAMETER);
        if (!checkInput.apply(dateString)) {
            throw new DateTimeParseException(ERROR_WRONG_DATE, dateString, 0);
        }
        oldValues.put(DELIVERY_DATE_PARAMETER, dateString);
        return LocalDate.parse(dateString, formatter);
    }

    private void setRequestErrors(HttpServletRequest request) {
        Map<String, String> errors = getSessionMap(request.getSession(), ERRORS_SESSION_ATTRIBUTE);
        Map<String, String> oldValues = getSessionMap(request.getSession(), OLD_VALUES_SESSION_ATTRIBUTE);
        request.setAttribute(ERRORS_SESSION_ATTRIBUTE, errors);
        request.setAttribute(OLD_VALUES_SESSION_ATTRIBUTE, oldValues);
    }

    private void clearSessionErrors(HttpServletRequest request) {
        setSessionMap(request.getSession(), new HashMap<>(), ERRORS_SESSION_ATTRIBUTE);
        setSessionMap(request.getSession(), new HashMap<>(), OLD_VALUES_SESSION_ATTRIBUTE);
    }

    private Map<String, String> getSessionMap(HttpSession session, String mapAttribute) {
        synchronized (session) {
            Map<String, String> map =
                    (Map<String, String>) session.getAttribute(mapAttribute);
            if (map == null) {
                map = new HashMap<>();
            }
            return map;
        }
    }

    private void setSessionMap(HttpSession session, Map<String, String> map, String mapAttribute) {
        synchronized (session) {
            if (map == null) {
                map = new HashMap<>();
            }
            session.setAttribute(mapAttribute, map);
        }
    }
}
