package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    @Mock
    private Cart cart;
    @Mock
    private Order orderMock;

    private static final int DEFAULT_QUANTITY = 1;
    private Product defaultProduct;
    private CartItem defaultCartItem;
    private Order order;
    private OrderService orderService;

    @Before
    public void setup() {
        cart = new Cart();
        defaultProduct = new Product();
        defaultCartItem = new CartItem(defaultProduct, DEFAULT_QUANTITY);
        cart.setItems(Arrays.asList(defaultCartItem));
        orderService = DefaultOrderService.getInstance();
    }

    @Test
    public void testGetOrder() {
        order = orderService.getOrder(cart);

        assertNotSame(order.getItems(), cart.getItems());
        assertEquals(order.getItems().size(), cart.getItems().size());
        assertArrayEquals(order.getItems().toArray(), cart.getItems().toArray());
        List<CartItem> orderItems = order.getItems();
        List<CartItem> cartItems = cart.getItems();
        for (int i = 0; i < orderItems.size(); ++i) {
            assertNotSame(orderItems.get(i), cartItems.get(i));
        }
        BigDecimal totalCost = cart.getTotalCost().add(calculateDeliveryCost());
        assertEquals(totalCost, order.getTotalCost());
    }

    @Test
    public void testPlaceOrder() {
        orderService.placeOrder(orderMock);

        verify(orderMock).setSecureId(anyString());
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(10);
    }
}
