package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_FIELD_PARAMETER = "sortField";
    private static final String SORT_ORDER_PARAMETER = "sortOrder";
    private static final String PRODUCT_LIST_PARAMETER = "products";
    private static final String PRODUCT_LIST_PAGE_PATH
            = "/WEB-INF/pages/productList.jsp";


    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(QUERY_PARAMETER);
        SortField sortField = SortField.fromName(request.getParameter(SORT_FIELD_PARAMETER));
        SortOrder sortOrder = SortOrder.fromName(request.getParameter(SORT_ORDER_PARAMETER));
        request.setAttribute(PRODUCT_LIST_PARAMETER, productDao.findProducts(query, sortField, sortOrder));
        request.getRequestDispatcher(PRODUCT_LIST_PAGE_PATH).forward(request, response);
    }
}
