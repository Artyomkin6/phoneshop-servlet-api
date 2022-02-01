package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String PRODUCT_DETAILS_PAGE_PATH
            = "/WEB-INF/pages/productDetails.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getPathInfo().substring(1));
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(id));
        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE_PATH).forward(request, response);
    }
}
