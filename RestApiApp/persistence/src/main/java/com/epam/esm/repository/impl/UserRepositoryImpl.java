package com.epam.esm.repository.impl;

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

    private static final String JPQL_GET_BY_EMAIL = "SELECT u FROM User u where u.email = :email";

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

        conditions = criteriaBuilder.and(conditions, createPredicates(criteria, userRoot));

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, User.class);
        if (count == 0L){
            return new PagedDTO<>();
        }

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }
        criteriaQuery.where(conditions);

        CriteriaUtil.applySortingParams(entityManager, userRoot, criteriaQuery, criteria.getSortingField().attribute,
                criteria.getSortingOrder());

        TypedQuery<User> typedQuery = entityManager.createQuery(criteriaQuery);
        List<User> resultList = typedQuery.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize).getResultList();
        return new PagedDTO<>(resultList, metadata);

    }

    @Override
    @Transactional
    public User getByEmail(String email) throws RepositoryException {
        return entityManager.createQuery(JPQL_GET_BY_EMAIL, User.class).setParameter(User_.EMAIL, email).getResultStream()
                .findAny().orElseThrow(DataNotExistRepositoryException::new);
    }

    @Override
    public User getById(Long id) throws RepositoryException {
        return Optional.ofNullable(entityManager.find(User.class, id)).orElseThrow(DataNotExistRepositoryException::new);
    }

    @Override
    public boolean isEmailExists(String email){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(criteriaBuilder.count(userRoot));
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(User_.email), email));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult() > 0;
    }

    @Override
    public boolean isIdExists(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.select(criteriaBuilder.count(userRoot));
        criteriaQuery.where(criteriaBuilder.equal(userRoot.get(User_.id), id));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult() > 0;
    }

    @Override
    @Transactional
    public void add(User user) {
        entityManager.persist(user);
    }

    private Predicate createPredicates(UserCriteria criteria, Root<User> userRoot){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate conditions = criteriaBuilder.conjunction();
        if (!criteria.getFirstNamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.like(userRoot.get(User_.firstName),criteria.getFirstNamePart() + "%"));
        }
        if (!criteria.getLastNamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.like(userRoot.get(User_.lastName),criteria.getLastNamePart() + "%"));
        }
        return conditions;
    }

}
