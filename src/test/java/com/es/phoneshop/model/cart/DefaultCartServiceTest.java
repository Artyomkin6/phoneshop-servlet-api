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
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
        product.setPrice(BigDecimal.ZERO);
        productDao.deleteAll();
        productDao.save(product);
        when(request.getSession()).thenReturn(session);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testGetCartWhenSessionCartIsNullThenNewCartWillBeCreated() {
        when(session.getAttribute(anyString())).thenReturn(null);

        Cart currentCart = cartService.getCart(session);

        assertNotNull(currentCart);
    }

    @Test
    public void testGetCartWhenSessionCartIsNotNullThenCartWillBeReturned() {
        when(session.getAttribute(anyString())).thenReturn(cart);

        Cart currentCart = cartService.getCart(session);

        assertSame(cart, currentCart);
    }

    @Test
    public void testAddWhenQuantityIsAppropriateThenNormalBehavior()
            throws WrongQuantityException, NotEnoughStockException {
        product.setStock(20);
        int quantity = 10;

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
    public void testAddWhenProductExistsThenQuantityAdded()
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
    public void testAddWhenQuantityIsNegativeThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int wrongQuantity = -1;
        product.setStock(20);
        exceptionRule.expect(WrongQuantityException.class);

        cartService.add(cart, PRODUCT_ID, wrongQuantity);
    }

    @Test
    public void testAddWhenQuantityIsZeroThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int wrongQuantity = 0;
        product.setStock(20);
        exceptionRule.expect(WrongQuantityException.class);

        cartService.add(cart, PRODUCT_ID, wrongQuantity);
    }

    @Test
    public void testAddWhenNotEnoughStockThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        product.setStock(10);
        int quantity = 20;
        exceptionRule.expect(NotEnoughStockException.class);

        cartService.add(cart, PRODUCT_ID, quantity);
    }

    @Test
    public void testUpdateWhenQuantityIsAppropriateThenNormalBehavior()
            throws WrongQuantityException, NotEnoughStockException {
        product.setStock(20);
        int quantity = 10;
        cart.getItems().add(new CartItem(product, quantity));
        int newQuantity = 20;

        cartService.update(cart, PRODUCT_ID, newQuantity);

        CartItem currentCartItem = cart.getItems().stream()
                .findAny()
                .orElse(null);
        assertNotNull(currentCartItem);
        Product currentProduct = currentCartItem.getProduct();
        assertSame(product, currentProduct);
        assertEquals(newQuantity, currentCartItem.getQuantity());
    }

    @Test
    public void testUpdateWhenQuantityIsNegativeThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int cartQuantity = 1;
        int wrongQuantity = -1;
        product.setStock(20);
        cart.getItems().add(new CartItem(product, cartQuantity));
        exceptionRule.expect(WrongQuantityException.class);

        cartService.update(cart, PRODUCT_ID, wrongQuantity);
    }

    @Test
    public void testUpdateWhenQuantityIsZeroThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int cartQuantity = 1;
        int wrongQuantity = 0;
        product.setStock(20);
        cart.getItems().add(new CartItem(product, cartQuantity));
        exceptionRule.expect(WrongQuantityException.class);

        cartService.update(cart, PRODUCT_ID, wrongQuantity);
    }

    @Test
    public void testUpdateWhenNotEnoughStockThenExpectException()
            throws WrongQuantityException, NotEnoughStockException {
        int cartQuantity = 1;
        product.setStock(20);
        int bigQuantity = 50;
        cart.getItems().add(new CartItem(product, cartQuantity));
        exceptionRule.expect(NotEnoughStockException.class);

        cartService.update(cart, PRODUCT_ID, bigQuantity);
    }

    @Test
    public void testDeleteWhenProductExistsThenItShouldBeDeleted() {
        int cartQuantity = 1;
        cart.getItems().add(new CartItem(product, cartQuantity));

        cartService.delete(cart, PRODUCT_ID);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testDeleteWhenProductDoesNotExistThenNothingShouldBeDone() {
        int cartQuantity = 1;
        cart.getItems().add(new CartItem(product, cartQuantity));
        Long nonExistingId = Long.MAX_VALUE;
        int sizeBeforeDeleting = cart.getItems().size();

        cartService.delete(cart, nonExistingId);

        assertFalse(cart.getItems().isEmpty());
        assertEquals(sizeBeforeDeleting, cart.getItems().size());
    }
}
