package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
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
import java.util.HashMap;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpServletResponse response;

    private static final String ERRORS_SESSION_ATTRIBUTE = "cartErrors";
    private static final String QUANTITIES_SESSION_ATTRIBUTE = "errorQuantities";
    private static final String CART_SESSION_ATTRIBUTE
            = DefaultCartService.class.getName() + ".class";
    private static final String ERROR_PATH_FORMAT = "/cart";

    private Cart cart;
    private CartPageServlet servlet = new CartPageServlet();
    private ProductDao productDao = ArrayListProductDao.getInstance();
    private String[] productIds;
    private String[] quantities;

    @Before
    public void setup() throws ServletException {
        servlet.init();

        ((ArrayListProductDao) productDao).deleteAll();
        cart = new Cart();
        Product product = new Product();
        product.setId(1L);
        product.setStock(1);
        product.setPrice(BigDecimal.ZERO);
        cart.getItems().add(new CartItem(product, 1));
        productDao.save(product);

        productIds = new String[1];
        quantities = new String[1];

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(cart);
        when(session.getAttribute(ERRORS_SESSION_ATTRIBUTE)).thenReturn(new HashMap<Long, String>());
        when(session.getAttribute(QUANTITIES_SESSION_ATTRIBUTE)).thenReturn(new HashMap<Long, String>());
        when(request.getLocale()).thenReturn(Locale.UK);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithoutErrors() throws IOException, ServletException {
        productIds[0] = "1";
        quantities[0] = "1";
        when(request.getParameterValues(anyString())).thenReturn(productIds, quantities);

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWhenQuantityIsNotPositive() throws IOException, ServletException {
        productIds[0] = "1";
        quantities[0] = "-1";
        when(request.getParameterValues(anyString())).thenReturn(productIds, quantities);

        servlet.doPost(request, response);

        verify(response).sendRedirect(endsWith(ERROR_PATH_FORMAT));
    }

    @Test
    public void testDoPostWhenQuantityIsWrong() throws IOException, ServletException {
        productIds[0] = "1";
        quantities[0] = "wrong";
        when(request.getParameterValues(anyString())).thenReturn(productIds, quantities);

        servlet.doPost(request, response);

        verify(response).sendRedirect(endsWith(ERROR_PATH_FORMAT));
    }

    @Test
    public void testDoPostWhenNotEnoughStock() throws IOException, ServletException {
        productIds[0] = "1";
        quantities[0] = "500";
        when(request.getParameterValues(anyString())).thenReturn(productIds, quantities);

        servlet.doPost(request, response);

        verify(response).sendRedirect(endsWith(ERROR_PATH_FORMAT));
    }
}
