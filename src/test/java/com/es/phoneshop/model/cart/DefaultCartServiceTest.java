package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Cart cart;
    @Mock
    private Product product;

    private static final Long PRODUCT_ID = 1L;

    private ArrayListProductDao productDao = ArrayListProductDao.getInstance();
    private CartService cartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        cart = new Cart();
        product = new Product();
        product.setId(PRODUCT_ID);
        productDao.deleteAll();
        productDao.save(product);
        when(request.getSession()).thenReturn(session);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testWhenSessionCartIsNullThenNewCartWillBeCreated() {
        when(session.getAttribute(anyString())).thenReturn(null);

        Cart currentCart = cartService.getCart(request);

        assertNotNull(currentCart);
    }

    @Test
    public void testWhenSessionCartIsNotNullThenCartWillBeReturned() {
        when(session.getAttribute(anyString())).thenReturn(cart);

        Cart currentCart = cartService.getCart(request);

        assertSame(cart, currentCart);
    }

    @Test
    public void testWhenQuantityIsAppropriateThenNormalBehavior()
            throws WrongQuantityException, NotEnoughStockException {
        product.setStock(20);
        int quantity = 10;

        System.out.println(Objects.isNull(cart));
        cartService.add(cart, PRODUCT_ID, quantity);

        CartItem addedCartItem = cart.getItems().stream()
                .findAny()
                .orElse(null);
        assertNotNull(addedCartItem);
        Product addedProduct = addedCartItem.getProduct();
        assertSame(product, addedProduct);
        assertEquals(quantity, addedCartItem.getQuantity());
    }

    @Test
    public void testWhenProductExistsThenQuantityAdded()
            throws WrongQuantityException, NotEnoughStockException {
        int quantity = 5;
        product.setStock(20);
        CartItem currentCartItem = new CartItem(product, quantity);
        cart.getItems().add(currentCartItem);
        int expectedItemsAmount = 1;

        cartService.add(cart, PRODUCT_ID, quantity);

        assertEquals(expectedItemsAmount, cart.getItems().size());
        CartItem addedCartItem = cart.getItems().stream()
                .findAny()
                .orElse(null);
        assertSame(currentCartItem, addedCartItem);
    }

    @Test
    public void testWhenQuantityIsNegativeThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int wrongQuantity = -1;
        product.setStock(20);
        exceptionRule.expect(WrongQuantityException.class);

        cartService.add(cart, PRODUCT_ID, wrongQuantity);

    }

    @Test
    public void testWhenQuantityIsZeroThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int wrongQuantity = 0;
        product.setStock(20);
        exceptionRule.expect(WrongQuantityException.class);

        cartService.add(cart, PRODUCT_ID, wrongQuantity);

    }

    @Test
    public void testWhenNotEnoughStockThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        product.setStock(10);
        int quantity = 20;
        exceptionRule.expect(NotEnoughStockException.class);

        cartService.add(cart, PRODUCT_ID, quantity);

    }
}