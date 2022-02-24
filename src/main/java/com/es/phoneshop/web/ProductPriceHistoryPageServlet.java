package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.utils.ProductUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String HISTORIES_ATTRIBUTE = "histories";
    private static final String PRODUCT_PRICE_HISTORY_PAGE_PATH
            = "/WEB-INF/pages/productPriceHistory.jsp";

    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getPathInfo().substring(1));
        Product product = productDao.getById(id);
        request.setAttribute(PRODUCT_ATTRIBUTE, product);
        request.setAttribute(HISTORIES_ATTRIBUTE, ProductUtil.getSortedHistories(product));
        request.getRequestDispatcher(PRODUCT_PRICE_HISTORY_PAGE_PATH).forward(request, response);
    }
}
