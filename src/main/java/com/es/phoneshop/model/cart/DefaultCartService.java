package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE
            = DefaultCartService.class.getName() + ".class";
    private final ReentrantLock LOCK = new ReentrantLock();

    private static CartService instance;
    private Cart cart;
    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (request.getSession()) {
            cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (Objects.isNull(cart)) {
                cart = new Cart();
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException, WrongQuantityException {
        validateQuantity(quantity);
        checkStock(cart, productId, quantity);

        LOCK.lock();
        try {
            CartItem currentItem = cart.getItems().stream()
                    .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                    .findAny()
                    .orElse(null);
            if (Objects.isNull(currentItem)) {
                Product currentProduct = productDao.getProduct(productId);
                currentItem = new CartItem(currentProduct, quantity);
                cart.getItems().add(currentItem);
            } else {
                currentItem.setQuantity(currentItem.getQuantity() + quantity);
            }
        } finally {
            LOCK.unlock();
        }
    }

    private void validateQuantity(int quantity) throws WrongQuantityException {
        if (quantity <= 0) {
            throw new WrongQuantityException();
        }
    }

    private void checkStock(Cart cart, Long productId, int quantity) throws NotEnoughStockException {
        Product currentProduct = productDao.getProduct(productId);
        int cartQuantity = getProductQuantity(cart, productId);
        boolean enoughStock =
                (currentProduct.getStock() >= (quantity + cartQuantity));
        if (!enoughStock) {
            throw new NotEnoughStockException();
        }
    }

    private int getProductQuantity(Cart cart, Long productId) {
        CartItem currentItem = cart.getItems().stream()
                .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                .findAny()
                .orElse(null);
        if (!Objects.isNull(currentItem)) {
            return currentItem.getQuantity();
        } else {
            return 0;
        }
    }
}
