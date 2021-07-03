package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.Tag_;
import com.epam.esm.exception.DataAlreadyExistRepositoryException;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository{

    private static final String JPQL_GET_BY_NAME = "SELECT t from Tag t where t.name = :name";
    private static final String JPQL_GET_ALL = "SELECT t FROM Tag t";
    private final EntityManager entityManager;
    
    @Autowired
    public TagRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<Tag> getAll() {
        return entityManager.createQuery(JPQL_GET_ALL, Tag.class).getResultList();
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
