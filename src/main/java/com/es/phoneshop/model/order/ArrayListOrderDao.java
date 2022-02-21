package com.es.phoneshop.model.order;

import com.es.phoneshop.model.abstract_dao.AbstractArrayListDao;
import com.es.phoneshop.model.abstract_dao.ItemNotFoundException;

public class ArrayListOrderDao extends AbstractArrayListDao<Order> implements OrderDao {
    private static ArrayListOrderDao instance;

    private ArrayListOrderDao() {
        super();
    }

    public static synchronized ArrayListOrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    @Override
    public Order getById(Long id) throws OrderNotFoundException {
        try {
            return super.getById(id);
        } catch (ItemNotFoundException exception) {
            throw new OrderNotFoundException(exception.getId());
        }
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        LOCK.readLock().lock();
        try {
            return items.stream()
                    .filter(item -> secureId.equals(item.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException(secureId));
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    protected void add(Order item) {
        items.add(item);
    }
}
