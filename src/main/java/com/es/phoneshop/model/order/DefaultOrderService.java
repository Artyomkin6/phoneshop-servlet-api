package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private static OrderService instance;
    private OrderDao orderDao;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new DefaultOrderService();
        }
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream()
                .map(cartItem -> {
                    try {
                        return (CartItem) cartItem.clone();
                    } catch (CloneNotSupportedException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toList())
        );
        order.setCurrency(cart.getCurrency());
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setSubtotalCost(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotalCost().add(order.getDeliveryCost()));
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(10);
    }
}
