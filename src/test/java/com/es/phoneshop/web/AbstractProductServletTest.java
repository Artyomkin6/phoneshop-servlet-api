package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractProductServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    private static final String CART_SESSION_ATTRIBUTE
            = DefaultCartService.class.getName() + ".class";
    private final Long PRODUCT_ID = 1L;
    private final int PRODUCT_STOCK = 10;

    private ProductDao productDao = ArrayListProductDao.getInstance();
    private AbstractProductServlet servlet = new ProductDetailsPageServlet();
    private Product product;
    private Cart cart;

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(Locale.UK);

        cart = new Cart();
        when(session.getAttribute(CART_SESSION_ATTRIBUTE)).thenReturn(cart);

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
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testAddProductToCartWhenQuantityIsNegativeThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = "-1";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testAddProductToCartWhenQuantityIsZeroThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = "0";
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testAddProductToCartWhenNotEnoughStockThenErrorHandle()
            throws ServletException, IOException {
        String quantityString = String.valueOf(PRODUCT_STOCK + 1);
        when(request.getParameter(anyString())).thenReturn(quantityString);

        servlet.addProductToCart(request, response, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
        verify(response).sendRedirect(anyString());
    }
}
