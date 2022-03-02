package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;

public class ProductListPageServlet extends AbstractProductServlet {
    private static final String PRODUCT_LIST_ATTRIBUTE = "products";
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_FIELD_PARAMETER = "sortField";
    private static final String SORT_ORDER_PARAMETER = "sortOrder";
    private static final String PRODUCT_ID_PARAMETER = "productId";
    private static final String PRODUCT_LIST_PAGE_PATH
            = "/WEB-INF/pages/productList.jsp";
    private static final String REDIRECT_PATH_FORMAT
            = "%s/products?addedProductId=%d&message=Added to cart successfully";
    private static final String ERROR_PATH_FORMAT
            = "%s/products?errorProductId=%d&errorQuantity=%s&error=%s";

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(QUERY_PARAMETER);
        SortField sortField = SortField.fromName(request.getParameter(SORT_FIELD_PARAMETER));
        SortOrder sortOrder = SortOrder.fromName(request.getParameter(SORT_ORDER_PARAMETER));

        request.setAttribute(PRODUCT_LIST_ATTRIBUTE, productDao.findProducts(query, sortField, sortOrder));

        Queue<Product> recentProducts = recentProductsService.getRecentProducts(request.getSession());
        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentProducts);

        request.getRequestDispatcher(PRODUCT_LIST_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productId = Long.valueOf(request.getParameter(PRODUCT_ID_PARAMETER));
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
}
