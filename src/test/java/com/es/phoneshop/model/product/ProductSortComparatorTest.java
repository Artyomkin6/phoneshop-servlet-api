package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductSortComparatorTest {
    private ProductSortComparator comparator;
    private Product defaultProduct1;
    private Product defaultProduct2;

    @Before
    public void setup() {
        defaultProduct1 = new Product();
        defaultProduct2 = new Product();
    }

    @Test
    public void testAscendingByDescription() {
        comparator = new ProductSortComparator(
                SortField.DESCRIPTION, SortOrder.ASCENDING);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertTrue(compareResult > 0);
    }

    @Test
    public void testDescendingByDescription() {
        comparator = new ProductSortComparator(
                SortField.DESCRIPTION, SortOrder.DESCENDING);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertTrue(compareResult < 0);
    }

    @Test
    public void testAscendingByPrice() {
        comparator = new ProductSortComparator(
                SortField.PRICE, SortOrder.ASCENDING);
        defaultProduct1.setPrice(new BigDecimal(200));
        defaultProduct2.setPrice(new BigDecimal(100));

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertTrue(compareResult > 0);
    }

    @Test
    public void testDescendingByPrice() {
        comparator = new ProductSortComparator(
                SortField.PRICE, SortOrder.DESCENDING);
        defaultProduct1.setPrice(new BigDecimal(200));
        defaultProduct2.setPrice(new BigDecimal(100));

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertTrue(compareResult < 0);
    }

    @Test
    public void testWhenSortFieldIsNullProductsAreEqual() {
        comparator = new ProductSortComparator(null, SortOrder.ASCENDING);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");
        defaultProduct1.setPrice(new BigDecimal(200));
        defaultProduct2.setPrice(new BigDecimal(100));

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertEquals(0, compareResult);
    }

    @Test
    public void testWhenSortOrderIsNullProductsAreEqual() {
        comparator = new ProductSortComparator(SortField.PRICE, null);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");
        defaultProduct1.setPrice(new BigDecimal(200));
        defaultProduct2.setPrice(new BigDecimal(100));

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertEquals(0, compareResult);
    }

    @Test
    public void testWhenSortFieldAndOrderAreNullProductsAreEqual() {
        comparator = new ProductSortComparator(null, null);
        defaultProduct1.setDescription("Samsung Galaxy S");
        defaultProduct2.setDescription("Apple iphone");
        defaultProduct1.setPrice(new BigDecimal(200));
        defaultProduct2.setPrice(new BigDecimal(100));

        int compareResult = comparator.compare(defaultProduct1, defaultProduct2);

        assertEquals(0, compareResult);
    }
}
