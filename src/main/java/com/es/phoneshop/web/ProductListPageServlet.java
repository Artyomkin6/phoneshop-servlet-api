package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.recent_products.DefaultRecentProductsService;
import com.es.phoneshop.model.recent_products.RecentProductsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCT_LIST_ATTRIBUTE = "products";
    private static final String RECENT_PRODUCTS_ATTRIBUTE = "recentProducts";
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_FIELD_PARAMETER = "sortField";
    private static final String SORT_ORDER_PARAMETER = "sortOrder";
    private static final String PRODUCT_LIST_PAGE_PATH
            = "/WEB-INF/pages/productList.jsp";

    private ProductDao productDao;
    private RecentProductsService recentProductsService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        recentProductsService = DefaultRecentProductsService.getInstance();
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
}
