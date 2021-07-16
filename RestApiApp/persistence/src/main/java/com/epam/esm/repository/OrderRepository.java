package com.epam.esm.repository;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.RepositoryException;


/**
 * Repository for manipulating orders data in database
 */
public interface OrderRepository {


    /**
     * Method for adding created order to database
     * @param order order to persist
     * @return created order with the generated data
     */
    Order makeOrder(Order order);

    /**
     * Gets page with order of a user form database by criteria
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @param userEmail - email of a user for orders searching
     * @return page with orders which match the criteria
     * @throws RepositoryException if no such page or some troubles in database were happened
     */
    PagedDTO<Order> getOrders(String userEmail, OrderCriteria criteria, int pageSize, int pageNumber) throws RepositoryException;

    /**
     * Get order of a user by its id
     * @param userEmail email of a user
     * @param orderId id of users order
     * @return order found
     * @throws RepositoryException if order wasn't found or some troubles in database were happened
     */
    Order getOrder(String userEmail, Long orderId) throws RepositoryException;
}
