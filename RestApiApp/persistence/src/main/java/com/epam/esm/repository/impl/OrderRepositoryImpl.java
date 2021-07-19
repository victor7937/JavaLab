package com.epam.esm.repository.impl;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.*;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.IncorrectPageRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.CriteriaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String JPQL_GET_USERS_ORDER = "SELECT o from Order o where o.user.id = :u_id and o.id = :o_id";
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
    public PagedDTO<Order> getOrders(Long userId, OrderCriteria criteria, int pageSize, int pageNumber) throws RepositoryException {
        if (!userRepository.isIdExists(userId)){
            throw new DataNotExistRepositoryException();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        Join<Order, User> orderJoin = orderRoot.join(Order_.user);
        Predicate conditions = criteriaBuilder.equal(orderJoin.get(User_.id), userId);
        criteriaQuery.select(orderRoot).distinct(true);

        conditions = criteriaBuilder.and(conditions, createPredicates(criteria, orderRoot));

        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(Order.class).join(Order_.user)));
        entityManager.createQuery(cq);
        cq.where(conditions);
        Long count = entityManager.createQuery(cq).getSingleResult();

        if (count == 0L){
            return new PagedDTO<>();
        }

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);

        CriteriaUtil.applySortingParams(entityManager, orderRoot, criteriaQuery, criteria.getSortingField().attribute,
                criteria.getSortingOrder());

        TypedQuery<Order> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Order> resultList = typedQuery.setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        return new PagedDTO<>(resultList, metadata);
    }

    @Override
    @Transactional
    public Order getOrder(Long userId, Long orderId) throws RepositoryException {
        return entityManager.createQuery(JPQL_GET_USERS_ORDER, Order.class)
                .setParameter("o_id", orderId).setParameter("u_id", userId).getResultStream()
                .findAny().orElseThrow(DataNotExistRepositoryException::new);
    }

    private Predicate createPredicates(OrderCriteria criteria, Root<Order> orderRoot){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate conditions = criteriaBuilder.conjunction();
        conditions = criteriaBuilder.and(conditions, criteriaBuilder.between(orderRoot.get(Order_.cost),
                criteria.getMinCost(), criteria.getMaxCost()));
        conditions = criteriaBuilder.and(conditions, criteriaBuilder.between(orderRoot.get(Order_.timeOfPurchase),
                criteria.getMinTime(), criteria.getMaxTime()));
        return conditions;
    }

}
