package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Cart cart;

    private static final int DEFAULT_QUANTITY = 1;
    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(10);
    private static final String DEFAULT_NAME = "Name";
    private static final String DEFAULT_PHONE = "+123";
    private static final String DEFAULT_STRING_DATE = "22.02.2022";
    private static final String DEFAULT_STRING_PAYMENT_METHOD = "cash";
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String FIRST_NAME_PARAMETER = "firstName";
    private static final String LAST_NAME_PARAMETER = "lastName";
    private static final String PHONE_PARAMETER = "phone";
    private static final String DELIVERY_DATE_PARAMETER = "deliveryDate";
    private static final String ADDRESS_PARAMETER = "address";
    private static final String PAYMENT_METHOD_PARAMETER = "paymentMethod";
    private static final String SUCCESS_PATH_FORMAT = "/order/overview/";
    private static final String ERROR_PATH_FORMAT = "/checkout";
    private static final String CART_SESSION_ATTRIBUTE
            = DefaultCartService.class.getName() + ".class";
    private static final String ERRORS_SESSION_ATTRIBUTE = "checkoutErrors";
    private static final String OLD_VALUES_SESSION_ATTRIBUTE = "oldValues";

    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(cart);
        when(session.getAttribute(ERRORS_SESSION_ATTRIBUTE)).thenReturn(new HashMap<String, String>());
        when(session.getAttribute(OLD_VALUES_SESSION_ATTRIBUTE)).thenReturn(new HashMap<String, String>());
        constructCart(cart);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getContextPath()).thenReturn("");
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ORDER_ATTRIBUTE), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWhenAllParametersFilledThenRedirectToSuccessPath() throws ServletException, IOException {
        setOrderInputInRequest(request);

        servlet.doPost(request, response);

        verify(response).sendRedirect(startsWith(SUCCESS_PATH_FORMAT));
    }

    @Test
    public void testDoPostWhenThereAreErrorsThenRedirectToErrorPath() throws ServletException, IOException {
        when(request.getParameter(anyString())).thenReturn("");

        servlet.doPost(request, response);

        verify(response).sendRedirect(eq(ERROR_PATH_FORMAT));
    }

    private void constructCart(Cart cart) {
        Product defaultProduct = new Product();
        CartItem defaultCartItem = new CartItem(defaultProduct, DEFAULT_QUANTITY);
        when(cart.getItems()).thenReturn(Arrays.asList(defaultCartItem));
        when(cart.getCurrency()).thenReturn(Currency.getInstance("USD"));
        when(cart.getTotalQuantity()).thenReturn(DEFAULT_QUANTITY);
        when(cart.getTotalCost()).thenReturn(DEFAULT_TOTAL_COST);
    }

    private void setOrderInputInRequest(HttpServletRequest request) {
        when(request.getParameter(FIRST_NAME_PARAMETER)).thenReturn(DEFAULT_NAME);
        when(request.getParameter(LAST_NAME_PARAMETER)).thenReturn(DEFAULT_NAME);
        when(request.getParameter(PHONE_PARAMETER)).thenReturn(DEFAULT_PHONE);
        when(request.getParameter(ADDRESS_PARAMETER)).thenReturn(DEFAULT_NAME);
        when(request.getParameter(DELIVERY_DATE_PARAMETER)).thenReturn(DEFAULT_STRING_DATE);
        when(request.getParameter(PAYMENT_METHOD_PARAMETER)).thenReturn(DEFAULT_STRING_PAYMENT_METHOD);
    }
}
