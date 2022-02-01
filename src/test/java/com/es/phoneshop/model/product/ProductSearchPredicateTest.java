package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ProductSearchPredicateTest {
    private ProductSearchPredicate predicate;
    private Product defaultProduct;

    @Before
    public void setup() {
        defaultProduct = new Product();
    }

    @Test
    public void testProductThatContainSearchMustBeFound() {
        String query = "Samsung";
        predicate = new ProductSearchPredicate(query);
        defaultProduct.setDescription("Samsung Galaxy S");

        boolean result = predicate.test(defaultProduct);

        assertTrue(result);
    }

    @Test
    public void testProductWithEmptyDescriptionMustNotBeFound() {
        String query = "Samsung";
        predicate = new ProductSearchPredicate(query);
        defaultProduct.setDescription("");

        boolean result = predicate.test(defaultProduct);

        assertFalse(result);
    }

    @Test
    public void testEmptyQueryFoundAllProducts() {
        String query = "";
        predicate = new ProductSearchPredicate(query);
        Product tempProduct1 = new Product();
        tempProduct1.setDescription("Hello");
        Product tempProduct2 = new Product();
        tempProduct2.setDescription("");

        boolean result1 = predicate.test(tempProduct1);
        boolean result2 = predicate.test(tempProduct2);

        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    public void testNullQueryFoundAllProducts() {
        predicate = new ProductSearchPredicate(null);
        Product tempProduct1 = new Product();
        tempProduct1.setDescription("Hello");
        Product tempProduct2 = new Product();
        tempProduct2.setDescription("");

        boolean result1 = predicate.test(tempProduct1);
        boolean result2 = predicate.test(tempProduct2);

        assertTrue(result1);
        assertTrue(result2);
    }
}
