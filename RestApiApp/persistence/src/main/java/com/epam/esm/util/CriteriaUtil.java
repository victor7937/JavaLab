package com.epam.esm.util;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class CriteriaUtil {

    public static Long getResultsCount(EntityManager entityManager, Predicate conditions, Class<?> tClass){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        cq.select(criteriaBuilder.count(cq.from(tClass)));
        entityManager.createQuery(cq);
        cq.where(conditions);
        return entityManager.createQuery(cq).getSingleResult();
    }
}
