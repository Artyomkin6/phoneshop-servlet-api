package com.es.phoneshop.web;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    private static final String SECURE_ORDER_ID = "admin";
    private static final String ORDER_ATTRIBUTE = "order";

    private Order order;
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init();
        order = new Order();
        orderDao.save(order);
        order.setSecureId(SECURE_ORDER_ID);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn('/' + SECURE_ORDER_ID);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        Order savedOrder = orderDao.getOrderBySecureId(SECURE_ORDER_ID);
        assertSame(order, savedOrder);
        verify(request).setAttribute(eq(ORDER_ATTRIBUTE), eq(order));
        verify(requestDispatcher).forward(request, response);
    }
}
