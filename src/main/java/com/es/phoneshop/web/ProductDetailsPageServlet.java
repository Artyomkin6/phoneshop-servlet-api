package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;

public class ProductDetailsPageServlet extends AbstractProductServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String PRODUCT_DETAILS_PAGE_PATH
            = "/WEB-INF/pages/productDetails.jsp";
    protected static final String REDIRECT_PATH_FORMAT
            = "%s/products/%d?message=Added to cart successfully";
    private static final String ERROR_PATH_FORMAT
            = "%s/products/%d?errorQuantity=%s&error=%s";

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);

        Queue<Product> recentProducts = recentProductsService.getRecentProducts(request.getSession());
        recentProductsService.addRecentProduct(recentProducts, productDao.getById(productId));
        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentProducts);

        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getById(productId));

        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        addProductToCart(request, response, productId);
    }

    @Override
    protected String getRedirectPath(HttpServletRequest request, Long productId) {
        return String.format(REDIRECT_PATH_FORMAT, request.getContextPath(), productId);
    }

    @Override
    protected String getErrorPathFormat(HttpServletRequest request, Long productId,
                                        String quantityString, String errorMessage) {
        return String.format(ERROR_PATH_FORMAT, request.getContextPath(), productId, quantityString, errorMessage);
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}
