package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.NotEnoughStockException;
import com.es.phoneshop.model.cart.WrongQuantityException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.recent_products.DefaultRecentProductsService;
import com.es.phoneshop.model.recent_products.RecentProductsService;
import com.es.phoneshop.web.helpers.InputHelper;
import com.es.phoneshop.web.helpers.QuantityInputHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public abstract class AbstractProductServlet extends HttpServlet {
    protected static final String QUANTITY_PARAMETER = "quantity";
    protected static final String RECENT_PRODUCTS_ATTRIBUTE = "recentProducts";
    protected static final String ERROR_MESSAGE_ATTRIBUTE = "error";
    protected static final String ERROR_PRODUCT_ID_ATTRIBUTE = "errorProductId";
    protected static final String ERROR_NOT_A_NUMBER = "Not a number";
    protected static final String ERROR_NOT_ENOUGH_STOCK = "Not enough stock";
    protected static final String ERROR_WRONG_QUANTITY
            = "Quantity must be a positive number";
    private static final InputHelper INPUT_HELPER = new QuantityInputHelper();

    protected ProductDao productDao;
    protected CartService cartService;
    protected RecentProductsService recentProductsService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentProductsService = DefaultRecentProductsService.getInstance();
    }

    protected void addProductToCart(HttpServletRequest request, HttpServletResponse response, Long productId) throws ServletException, IOException {
        int quantity;
        try {
            quantity = parseQuantity(request);
            cartService.add(cartService.getCart(request.getSession()), productId, quantity);
            response.sendRedirect(getRedirectPath(request, productId));
        } catch (ParseException exception) {
            handleError(request, response, productId, ERROR_NOT_A_NUMBER);
        } catch (WrongQuantityException exception) {
            handleError(request, response, productId, ERROR_WRONG_QUANTITY);
        } catch (NotEnoughStockException exception) {
            handleError(request, response, productId, ERROR_NOT_ENOUGH_STOCK);
        }
    }

    protected String getQuantityString(HttpServletRequest request) {
        return request.getParameter(QUANTITY_PARAMETER);
    }

    protected int parseQuantity(HttpServletRequest request) throws ParseException {
        String quantityString = getQuantityString(request);
        if (!INPUT_HELPER.CheckInput(quantityString)) {
            throw new ParseException(quantityString, 0);
        }
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(quantityString).intValue();
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response,
                             Long productId, String errorMessage)
            throws IOException {
        String quantityString = getQuantityString(request);
        response.sendRedirect(getErrorPathFormat(request, productId, quantityString, errorMessage));
    }

    protected abstract String getRedirectPath(HttpServletRequest request, Long productId);

    protected abstract String getErrorPathFormat(HttpServletRequest request, Long productId,
                                                 String quantityString, String errorMessage);
}
