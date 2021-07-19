package com.epam.esm.service;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ServiceException;


/**
 * Service for manipulating orders data
 */
public interface OrderService {


    /**
     * Create a new order of user for gift certificate
     * @param orderDTO - dto that contains users email and certificates id
     * @return created order
     * @throws ServiceException if dto data was incorrect or some troubles in data source were happened
     */
    Order makeOrder(OrderDTO orderDTO) throws ServiceException;

    /**
     * Gets page with orders of a user from data source
     * @param criteria - criteria with params for filtering and sorting
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @param userId - id of user for getting its orders
     * @return page with orders found
     * @throws ServiceException if criteria or pagination params are incorrect or some troubles in data source were happened
     */
    PagedDTO<Order> getOrdersOfUser(Long userId, OrderCriteria criteria, int pageSize, int pageNumber) throws ServiceException;

    /**
     * Get order of a user by its id
     * @param userId id of a user
     * @param orderId id of users order
     * @return order found
     * @throws ServiceException if email or id are incorrect, order wasn't found or some troubles in data source were happened
     */
    Order getOrder(Long userId, Long orderId) throws ServiceException;
}
