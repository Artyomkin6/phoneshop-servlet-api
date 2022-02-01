package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductNotFoundHandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductNotFoundException exception
                = (ProductNotFoundException) request.getAttribute("javax.servlet.error.exception");
        request.setAttribute("id", exception.getProductId());
        request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
    }
}
