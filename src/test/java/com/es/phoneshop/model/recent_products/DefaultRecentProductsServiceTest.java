package com.es.phoneshop.model.recent_products;

import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentProductsServiceTest {
    @Mock
    private HttpSession session;

    private Queue<Product> recentProducts;
    private RecentProductsService recentProductsService;

    @Before
    public void setup() {
        recentProductsService = DefaultRecentProductsService.getInstance();
        recentProducts = new LinkedList<>();
        when(session.getAttribute(anyString())).thenReturn(recentProducts);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testWhenRecentProductsIsNullThenNewWillBeCreated() {
        when(session.getAttribute(anyString())).thenReturn(null);

        recentProductsService.getRecentProducts(session);

        verify(session).setAttribute(anyString(), notNull());
    }

    @Test
    public void testWhenRecentProductsIsNotNullThenSessionAttributeWillBeReturned() {
        Queue<Product> currentRecentProducts;

        currentRecentProducts = recentProductsService.getRecentProducts(session);

        verify(session, never()).setAttribute(anyString(), notNull());
        assertSame(recentProducts, currentRecentProducts);
    }

    @Test
    public void testWhenRecentProductsContainsLessThanThreeThenNoProductWillBeRemoved() {
        Product queueProduct = new Product();
        Product addedProduct = new Product();
        recentProducts.add(queueProduct);

        recentProductsService.addRecentProduct(recentProducts, addedProduct);

        assertTrue(recentProducts.containsAll(
                Arrays.asList(queueProduct, addedProduct)));
    }

    @Test
    public void testWhenRecentProductsContainsThreeThenFirstProductWillBeRemoved() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product addedProduct = new Product();
        recentProducts.add(product1);
        recentProducts.add(product2);
        recentProducts.add(product3);

        recentProductsService.addRecentProduct(recentProducts, addedProduct);

        assertTrue(recentProducts.containsAll(
                Arrays.asList(product2, product3, addedProduct)));
    }
}