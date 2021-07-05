package com.epam.esm.repository.impl;

import com.epam.esm.criteria.SortingOrder;
import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.*;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.IncorrectPageRepositoryException;
import com.epam.esm.exception.RepositoryException;
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
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;

    private static final String JPQL_GET_ALL = "SELECT u FROM User u";

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public PagedDTO<User> getByCriteria(UserCriteria criteria, int pageSize, int pageNumber) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> userRoot = criteriaQuery.from(User.class);
        Predicate conditions = criteriaBuilder.conjunction();
        criteriaQuery.select(userRoot).distinct(true);

        if (!criteria.getNamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, createNamePartPredicate(criteria, userRoot));
        }
        if (!criteria.getSurnamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, createSurnamePartPredicate(criteria, userRoot));
        }

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, User.class);

        if (count == 0L){
            return new PagedDTO<>();
        }

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);

        Path<?> sortPath = userRoot.get(criteria.getSortingField().attribute);
        if (criteria.getSortingOrder() == SortingOrder.DESC) {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortPath));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortPath));
        }

        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);

        List<User> resultList = typedQuery.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize).getResultList();

        return new PagedDTO<>(resultList, metadata);

    }

    @Override
    @Transactional
    public User getByEmail(String email) throws RepositoryException {
        return Optional.ofNullable(entityManager.find(User.class, email)).orElseThrow(DataNotExistRepositoryException::new);
    }

    @Override
    public boolean isUserExists(String email){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(criteriaBuilder.count(userRoot));
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(User_.email), email));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult() > 0;
    }

    private Predicate createNamePartPredicate(UserCriteria criteria, Root<User> userRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.like(userRoot.get(User_.name),criteria.getNamePart() + "%");
    }

    private Predicate createSurnamePartPredicate(UserCriteria criteria, Root<User> userRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.like(userRoot.get(User_.surname),criteria.getSurnamePart() + "%");
    }

}
