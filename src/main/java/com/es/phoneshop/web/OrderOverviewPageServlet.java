package com.es.phoneshop.web;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_ATTRIBUTE = "order";
    private static final String ORDER_OVERVIEW_PAGE_PATH
            = "/WEB-INF/pages/orderOverview.jsp";

    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String secureOrderId = parseOrderId(request);
        Order order = orderDao.getOrderBySecureId(secureOrderId);
        request.setAttribute(ORDER_ATTRIBUTE, order);

        request.getRequestDispatcher(ORDER_OVERVIEW_PAGE_PATH).forward(request, response);
    }

    private String parseOrderId(HttpServletRequest request) {
        return request.getPathInfo().substring(1);
    }
}
