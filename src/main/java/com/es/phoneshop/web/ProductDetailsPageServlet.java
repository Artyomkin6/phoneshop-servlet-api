package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.cart.WrongQuantityException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.recent_products.DefaultRecentProductsService;
import com.es.phoneshop.model.recent_products.RecentProductsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Queue;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String CART_ATTRIBUTE = "cart";
    private static final String ERROR_MESSAGE_ATTRIBUTE = "error";
    private static final String RECENT_PRODUCTS_ATTRIBUTE = "recentProducts";
    private static final String QUANTITY_PARAMETER = "quantity";
    private static final String PRODUCT_DETAILS_PAGE_PATH
            = "/WEB-INF/pages/productDetails.jsp";
    private static final String ERROR_NOT_A_NUMBER = "Not a number";
    private static final String ERROR_NOT_ENOUGH_STOCK = "Not enough stock";
    private static final String ERROR_WRONG_QUANTITY
            = "Quantity must be a positive number";
    private static final String SUCCESS_MESSAGE = "Added to cart successfully";
    private static final String PATH_FORMAT = "%s/products/%d?message=%s";

    private ProductDao productDao;
    private CartService cartService;
    private RecentProductsService recentProductsService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentProductsService = DefaultRecentProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        Queue<Product> recentProducts = recentProductsService.getRecentProducts(request.getSession());
        recentProductsService.addRecentProduct(recentProducts, productDao.getProduct(productId));
        request.setAttribute(PRODUCT_ATTRIBUTE, productDao.getProduct(productId));
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));
        request.setAttribute(RECENT_PRODUCTS_ATTRIBUTE, recentProducts);
        request.getRequestDispatcher(PRODUCT_DETAILS_PAGE_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        int quantity;
        try {
            quantity = parseQuantity(request.getParameter(QUANTITY_PARAMETER), request);
        } catch (ParseException exception) {
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, ERROR_NOT_A_NUMBER);
            doGet(request, response);
            return;
        }

        try {
            cartService.add(cartService.getCart(request), productId, quantity);
            response.sendRedirect(
                    String.format(PATH_FORMAT, request.getContextPath(), productId, SUCCESS_MESSAGE)
            );
        } catch (WrongQuantityException exception) {
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, ERROR_WRONG_QUANTITY);
            doGet(request, response);
        } catch (NotEnoughStockException exception) {
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, ERROR_NOT_ENOUGH_STOCK);
            doGet(request, response);
        }
    }

    private int parseQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}
