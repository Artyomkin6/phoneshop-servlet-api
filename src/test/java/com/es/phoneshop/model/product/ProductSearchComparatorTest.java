package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductSearchComparatorTest {
    private ProductSearchComparator comparator;
    private Product defaultProduct1;
    private Product defaultProduct2;

    @Before
    public void setup() {
        defaultProduct1 = new Product();
        defaultProduct2 = new Product();
    }

    @Test
    public void testProductsWithMoreMatchWordsAreEarlier() {
        String query = "samsung s";
        comparator = new ProductSearchComparator(query);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertTrue(compareResult < 0);
    }

    @Test
    public void testProductsWithEqualDescriptionAreEqual() {
        String query = "samsung s";
        comparator = new ProductSearchComparator(query);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Samsung Galaxy S");

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertEquals(0, compareResult);
    }

    @Test
    public void testNullOrEmptyQueryGivesEqualProducts() {
        String query2 = "";
        Comparator<Product> comparator1 = new ProductSearchComparator(null);
        Comparator<Product> comparator2 = new ProductSearchComparator(query2);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");

        int compareResult1 = comparator1.compare(defaultProduct1, defaultProduct2);
        int compareResult2 = comparator2.compare(defaultProduct1, defaultProduct2);

        assertEquals(0, compareResult1);
        assertEquals(0, compareResult2);
    }
}
