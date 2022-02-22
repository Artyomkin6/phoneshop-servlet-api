package com.es.phoneshop.web;

import com.es.phoneshop.model.order.OrderNotFoundException;
import com.es.phoneshop.model.product.ProductNotFoundException;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderNotFoundHandlerServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    private static final String DEFAULT_IP = "1.0.0.1";
    private OrderNotFoundHandlerServlet servlet = new OrderNotFoundHandlerServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init();
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getAttribute(anyString())).thenReturn(new OrderNotFoundException(DEFAULT_IP));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("id"), any());
    }
}