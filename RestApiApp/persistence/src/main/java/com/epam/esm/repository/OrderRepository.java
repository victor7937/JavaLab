package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface OrderRepository {
    Order makeOrder(Order order);
    List<Order> getOrders(String userEmail) throws RepositoryException;
    Order getOrder(String userEmail, Long orderId) throws RepositoryException;
}
