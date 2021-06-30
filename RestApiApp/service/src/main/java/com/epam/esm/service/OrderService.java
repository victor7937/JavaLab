package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface OrderService {
    Order makeOrder(OrderDTO orderDTO) throws ServiceException;
    List<Order> getOrders(String userEmail) throws ServiceException;
    Order getOrder(String userEmail, Long orderId) throws ServiceException;
}
