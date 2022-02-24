package com.es.phoneshop.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private FilterConfig filterConfig;

    private static final String DEFAULT_IP = "1.0.0.1";
    private static final String DEFAULT_DOS_IP = "1.0.0.2";
    private DosFilter filter;

    @Before
    public void setup() throws ServletException {
        filter = new DosFilter();
        filter.init(filterConfig);
    }

    @Test
    public void testDoFilterWhenOneRequestThenItGoesNext() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn(DEFAULT_IP);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testDoFilterWhenTooManyRequestsThenSetErrorResponseStatus() throws ServletException, IOException {
        when(request.getRemoteAddr()).thenReturn(DEFAULT_DOS_IP);

        for (int i = 0; i < 30; ++i) {
            filter.doFilter(request, response, chain);
        }

        verify(response, atLeastOnce()).setStatus(eq(429));
    }
}