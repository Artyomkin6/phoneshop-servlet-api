package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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

    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new DefaultCartService();
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpSession session) {
        synchronized (session) {
            cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }

    @Override
    public void clearCart(HttpSession session) {
        synchronized (session) {
            session.setAttribute(CART_SESSION_ATTRIBUTE, new Cart());
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws NotEnoughStockException, WrongQuantityException {
        validateQuantity(quantity);
        checkAddStock(cart, productId, quantity);

        LOCK.lock();
        try {
            CartItem currentItem = cart.getItems().stream()
                    .filter(cartItem -> isProductInCartItemById(cartItem, productId))
                    .findAny()
                    .orElse(null);
            if (currentItem == null) {
                Product currentProduct = productDao.getById(productId);
                currentItem = new CartItem(currentProduct, quantity);
                cart.getItems().add(currentItem);
            } else {
                currentItem.setQuantity(currentItem.getQuantity() + quantity);
            }
            recalculateCart(cart);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws NotEnoughStockException, WrongQuantityException {
        validateQuantity(quantity);
        checkStock(productId, quantity);

        LOCK.lock();
        try {
            cart.getItems().stream()
                    .filter(cartItem -> isProductInCartItemById(cartItem, productId))
                    .forEach(cartItem -> cartItem.setQuantity(quantity));
            recalculateCart(cart);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        LOCK.lock();
        try {
            cart.getItems().removeIf(
                    cartItem -> isProductInCartItemById(cartItem, productId)
            );
            recalculateCart(cart);
        } finally {
            LOCK.unlock();
        }
    }

    private void validateQuantity(int quantity) throws WrongQuantityException {
        if (quantity <= 0) {
            throw new WrongQuantityException();
        }
    }

    private void checkStock(Long productId, int quantity) throws NotEnoughStockException {
        Product currentProduct = productDao.getById(productId);
        boolean enoughStock =
                (currentProduct.getStock() >= quantity);
        if (!enoughStock) {
            throw new NotEnoughStockException();
        }
    }

    private void checkAddStock(Cart cart, Long productId, int addQuantity) throws NotEnoughStockException {
        int cartQuantity = getProductQuantity(cart, productId);
        checkStock(productId, cartQuantity + addQuantity);
    }

    private int getProductQuantity(Cart cart, Long productId) {
        CartItem currentItem = cart.getItems().stream()
                .filter(cartItem -> productId.equals(cartItem.getProduct().getId()))
                .findAny()
                .orElse(null);
        if (currentItem != null) {
            return currentItem.getQuantity();
        } else {
            return 0;
        }
    }

    private void recalculateCart(Cart cart) {
        recalculateQuantity(cart);
        recalculateCost(cart);
        cart.getItems().stream()
                .findAny()
                .ifPresent(cartItem -> cart.setCurrency(cartItem.getProduct().getCurrency()));
    }

    private void recalculateQuantity(Cart cart) {
        int totalQuantity = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        cart.setTotalQuantity(totalQuantity);
    }

    private void recalculateCost(Cart cart) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            BigDecimal currentPrice = cartItem.getProduct().getPrice();
            int currentQuantity = cartItem.getQuantity();
            totalCost = sumNumbers(
                    totalCost, currentPrice.multiply(BigDecimal.valueOf(currentQuantity))
            );
        }

        cart.setTotalCost(totalCost);
    }

    private BigDecimal sumNumbers(BigDecimal number1, BigDecimal number2) {
        if (number1 == null && number2 == null) {
            return BigDecimal.ZERO;
        } else if (number1 == null) {
            return number2;
        } else if (number2 == null) {
            return number1;
        } else {
            return number1.add(number2);
        }
    }

    private boolean isProductInCartItemById(CartItem item, Long productId) {
        Product cartProduct = item.getProduct();
        if (cartProduct != null) {
            return productId.equals(cartProduct.getId());
        }
        return false;
    }
}
