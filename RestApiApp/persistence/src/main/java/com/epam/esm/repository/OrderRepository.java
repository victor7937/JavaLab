package com.epam.esm.repository;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface OrderRepository {
    Order makeOrder(Order order);
    PagedDTO<Order> getOrders(String userEmail, OrderCriteria criteria, int pageSize, int pageNumber) throws RepositoryException;
    Order getOrder(String userEmail, Long orderId) throws RepositoryException;
}
