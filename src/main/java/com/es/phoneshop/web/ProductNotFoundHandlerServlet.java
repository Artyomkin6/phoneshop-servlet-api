package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductNotFoundHandlerServlet extends HttpServlet {
    private static final String EXCEPTION_ATTRIBUTE
            = "javax.servlet.error.exception";
    private static final String ID_ATTRIBUTE = "id";
    private static final String PRODUCT_NOT_FOUND_PAGE_PATH
            = "/WEB-INF/pages/errorProductNotFound.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductNotFoundException exception
                = (ProductNotFoundException) request.getAttribute(EXCEPTION_ATTRIBUTE);
        request.setAttribute(ID_ATTRIBUTE, exception.getId());
        request.getRequestDispatcher(PRODUCT_NOT_FOUND_PAGE_PATH).forward(request, response);
    }
}
