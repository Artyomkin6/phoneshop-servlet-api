package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.recent_products.DefaultRecentProductsService;
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
import java.util.LinkedList;
import java.util.Locale;

import static com.es.phoneshop.web.AbstractProductServlet.ERROR_MESSAGE_ATTRIBUTE;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_NOT_A_NUMBER;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_NOT_ENOUGH_STOCK;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_PRODUCT_ID_ATTRIBUTE;
import static com.es.phoneshop.web.AbstractProductServlet.ERROR_WRONG_QUANTITY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractProductServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpSession session;

    private static final String CART_SESSION_ATTRIBUTE
            = DefaultCartService.class.getName() + ".class";
    private static final String RECENT_PRODUCTS_SESSION_ATTRIBUTE
            = DefaultRecentProductsService.class.getName() + ".class";
    private final Long PRODUCT_ID = 1L;
    private final int PRODUCT_STOCK = 10;

    private ProductDao productDao = ArrayListProductDao.getInstance();
    private AbstractProductServlet servlet = new ProductDetailsPageServlet();
    private Product product;
    private Cart cart;

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(Locale.UK);
        when(request.getPathInfo()).thenReturn('/' + PRODUCT_ID.toString());

        cart = new Cart();
        when(session.getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(cart);
        when(session.getAttribute(RECENT_PRODUCTS_SESSION_ATTRIBUTE))
                .thenReturn(new LinkedList<>());

        ((ArrayListProductDao) productDao).deleteAll();
        product = new Product();
        product.setId(PRODUCT_ID);
        product.setStock(PRODUCT_STOCK);
        product.setPrice(BigDecimal.ZERO);
        productDao.save(product);
    }

    @Test
    public void testAddProductToCartWhenQuantityIsAppropriateThenNormalBehavior()
            throws ServletException, IOException {
        String quantityString = "1";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertFalse(cart.getItems().isEmpty());
        Product cartProduct = cart.getItems().stream()
                .findAny()
                .get()
                .getProduct();
        assertSame(product, cartProduct);
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testAddProductToCartWhenQuantityIsNotANumberThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = "wrong";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(request).setAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), eq(ERROR_NOT_A_NUMBER));
        verify(request).setAttribute(eq(ERROR_PRODUCT_ID_ATTRIBUTE), eq(PRODUCT_ID));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testAddProductToCartWhenQuantityIsNegativeThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = "-1";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(request).setAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), eq(ERROR_WRONG_QUANTITY));
        verify(request).setAttribute(eq(ERROR_PRODUCT_ID_ATTRIBUTE), eq(PRODUCT_ID));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testAddProductToCartWhenQuantityIsZeroThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = "0";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(request).setAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), eq(ERROR_WRONG_QUANTITY));
        verify(request).setAttribute(eq(ERROR_PRODUCT_ID_ATTRIBUTE), eq(PRODUCT_ID));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testAddProductToCartWhenNotEnoughStockThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = String.valueOf(PRODUCT_STOCK + 1);
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(request).setAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), eq(ERROR_NOT_ENOUGH_STOCK));
        verify(request).setAttribute(eq(ERROR_PRODUCT_ID_ATTRIBUTE), eq(PRODUCT_ID));
        verify(requestDispatcher).forward(request, response);
    }
}