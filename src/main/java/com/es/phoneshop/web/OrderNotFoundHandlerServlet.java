package com.es.phoneshop.web;

import com.es.phoneshop.model.order.OrderNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderNotFoundHandlerServlet extends HttpServlet {
    private static final String EXCEPTION_ATTRIBUTE
            = "javax.servlet.error.exception";
    private static final String ID_ATTRIBUTE = "id";
    private static final String ORDER_NOT_FOUND_PAGE_PATH
            = "/WEB-INF/pages/errorOrderNotFound.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderNotFoundException exception
                = (OrderNotFoundException) request.getAttribute(EXCEPTION_ATTRIBUTE);
        if (!(exception.getId() == null)) {
            request.setAttribute(ID_ATTRIBUTE, exception.getId());
        } else {
            request.setAttribute(ID_ATTRIBUTE, exception.getSecureId());
        }
        request.getRequestDispatcher(ORDER_NOT_FOUND_PAGE_PATH).forward(request, response);
    }
}
