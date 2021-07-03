package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.zip.DataFormatException;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String JPQL_GET_USERS_ORDER = "SELECT o from Order o where o.user.email = :email and o.id = :id";
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    public OrderRepositoryImpl(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;

    }

    @Override
    @Transactional
    public Order makeOrder(Order order){
        entityManager.persist(order);
        return order;
    }

    @Override
    @Transactional
    public List<Order> getOrders(String userEmail) throws RepositoryException {
        List<Order> orders = userRepository.getByEmail(userEmail).getOrders();
        //orders.forEach(entityManager::detach);
        return orders;
    }

    @Override
    @Transactional
    public Order getOrder(String userEmail, Long orderId) throws RepositoryException {
        return entityManager.createQuery(JPQL_GET_USERS_ORDER, Order.class)
                .setParameter("id", orderId).setParameter("email", userEmail).getResultStream()
                .findAny().orElseThrow(DataNotExistRepositoryException::new);
    }
}
