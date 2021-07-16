package com.epam.esm.repository.impl;


import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.Tag_;
import com.epam.esm.exception.DataAlreadyExistRepositoryException;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.IncorrectPageRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.CriteriaUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository{

    private static final String JPQL_GET_BY_NAME = "SELECT t from Tag t where t.name = :name";

    private static final String SQL_GET_MOST_USED_TAG_OF_VALUABLE_CLIENT =
            "SELECT t.id, t.name, COUNT(t.id) as tg_count from user u" +
            " join orders o on u.email = o.users_email" +
            " join gift_certificate gc on gc.id = o.certificate_id" +
            " join m2m_certificate_tag m2mct on gc.id = m2mct.cert_id" +
            " join tag t on t.id = m2mct.tag_id where u.email =" +
            " (SELECT s.email from (SELECT SUM(o.cost) as s_cost, u.email as email from orders o" +
            " join user u on u.email = o.users_email group by u.email order by s_cost DESC LIMIT 1) s)" +
            " group by t.name order by tg_count desc LIMIT 1";

    private final EntityManager entityManager;
    
    @Autowired
    public TagRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public PagedDTO<Tag> get(String namePart, int pageSize, int pageNumber) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        Predicate conditions = criteriaBuilder.conjunction();
        criteriaQuery.select(tagRoot).distinct(true);

        if (!namePart.isBlank()){
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.like(tagRoot.get(Tag_.name),"%" + namePart + "%"));
        }

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, Tag.class);
        if (count == 0L){
            return new PagedDTO<>();
        }
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);
        criteriaQuery.orderBy(criteriaBuilder.asc(tagRoot.get(Tag_.name)));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(criteriaQuery);

        List<Tag> resultList = typedQuery.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize).getResultList();
        return new PagedDTO<>(resultList, metadata);
    }

    @Override
    @Transactional
    public Tag getById(Long id) throws RepositoryException {
        return Optional.ofNullable(entityManager.find(Tag.class, id)).orElseThrow(DataNotExistRepositoryException::new);
    }

    @Override
    @Transactional
    public Tag getByName(String name) {
        return entityManager.createQuery(JPQL_GET_BY_NAME, Tag.class).setParameter("name", name).getResultStream()
                .findAny().orElse(null);
    }

    @Override
    @Transactional
    public void add(Tag tag) throws RepositoryException {
        if (isTagExists(tag.getName())) {
            throw new DataAlreadyExistRepositoryException();
        }
        entityManager.persist(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RepositoryException {
       Tag tag = getById(id);
       entityManager.remove(tag);
    }

    @Override
    public Tag getMostUsedTagOfValuableCustomer() {
        List<Object[]> resultSet = entityManager.createNativeQuery(SQL_GET_MOST_USED_TAG_OF_VALUABLE_CLIENT).getResultList();
        Long tagId = ((BigInteger) resultSet.get(0)[0]).longValue();
        String tagName = (String) resultSet.get(0)[1];
        return new Tag(tagId, tagName);
    }

    @Override
    public boolean isTagExists(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);

        criteriaQuery.select(criteriaBuilder.count(tagRoot));
        criteriaQuery.where(criteriaBuilder.equal(tagRoot.get(Tag_.name), name));

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getSingleResult() > 0;
    }




}
