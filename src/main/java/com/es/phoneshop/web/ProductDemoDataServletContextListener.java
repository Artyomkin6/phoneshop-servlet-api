package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.DemoData;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ProductDemoDataServletContextListener implements ServletContextListener {
    private static final String DEMO_DATA_PARAMETER
            = "setDemoData";

    private ProductDao productDao;

    public ProductDemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String setDemoData = servletContextEvent
                .getServletContext()
                .getInitParameter(DEMO_DATA_PARAMETER);
        if (Boolean.parseBoolean(setDemoData)) {
            new DemoData().setDemoProducts(productDao);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
