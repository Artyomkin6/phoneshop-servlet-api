package com.es.phoneshop.web;

import com.es.phoneshop.model.product.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private static final String PRODUCT_LIST_ATTRIBUTE = "products";
    private static final String ERRORS_ATTRIBUTE = "errors";
    private static final String PRODUCT_CODE_PARAMETER = "productCode";
    private static final String MIN_PRICE_PARAMETER = "minPrice";
    private static final String MAX_PRICE_PARAMETER = "maxPrice";
    private static final String MIN_STOCK_PARAMETER = "minStock";
    private static final String ADVANCED_SEARCH_PAGE_PATH
            = "/WEB-INF/pages/advancedSearch.jsp";
    private static final String ERROR_WRONG_NUMBER = "Not a number";

    private ProductDao productDao;
    private Map<String, String> errors;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        errors = new HashMap<>();
        List<Product> products = setProducts(request);

        request.setAttribute(PRODUCT_LIST_ATTRIBUTE, products);
        request.setAttribute(ERRORS_ATTRIBUTE, errors);

        request.getRequestDispatcher(ADVANCED_SEARCH_PAGE_PATH).forward(request, response);
    }

    private List<Product> setProducts(HttpServletRequest request) {
        String productCode = request.getParameter(PRODUCT_CODE_PARAMETER);
        String minPriceString = request.getParameter(MIN_PRICE_PARAMETER);
        String maxPriceString = request.getParameter(MAX_PRICE_PARAMETER);
        String minStockString = request.getParameter(MIN_STOCK_PARAMETER);

        boolean isFirstAttendance = (productCode == null)
                        && (minPriceString == null)
                        && (maxPriceString == null)
                        && (minStockString == null);

        if (isFirstAttendance) {
            return new ArrayList<>();
        } else {
            BigDecimal minPrice = null;
            BigDecimal maxPrice = null;
            Integer minStock = null;
            try {
                minPrice = parsePrice(minPriceString);
            } catch (NumberFormatException exception) {
                errors.put(MIN_PRICE_PARAMETER, ERROR_WRONG_NUMBER);
            }
            try {
                maxPrice = parsePrice(maxPriceString);
            } catch (NumberFormatException exception) {
                errors.put(MAX_PRICE_PARAMETER, ERROR_WRONG_NUMBER);
            }
            try {
                minStock = parseStock(minStockString);
            } catch (NumberFormatException exception) {
                errors.put(MIN_STOCK_PARAMETER, ERROR_WRONG_NUMBER);
            }
            if (errors.isEmpty()) {
                return productDao.searchProducts(productCode, minPrice, maxPrice, minStock);
            } else {
                return new ArrayList<>();
            }
        }
    }

    private Integer parseStock(String stockString) throws NumberFormatException {
        if (!stockString.equals("")) {
            return Integer.parseInt(stockString);
        } else {
            return null;
        }
    }

    private BigDecimal parsePrice(String priceString) throws NumberFormatException {
        if (!priceString.equals("")) {
            return new BigDecimal(priceString);
        } else {
            return null;
        }
    }
}
