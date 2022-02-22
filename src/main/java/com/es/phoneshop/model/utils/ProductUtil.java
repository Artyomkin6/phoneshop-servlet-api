package com.es.phoneshop.model.utils;

import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.Product;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductUtil {
    public static List<PriceHistory> getSortedHistories(Product product) {
        return product.getHistories().stream()
                .sorted(Comparator.comparing(PriceHistory::getStartDate).reversed())
                .collect(Collectors.toList());
    }
}
