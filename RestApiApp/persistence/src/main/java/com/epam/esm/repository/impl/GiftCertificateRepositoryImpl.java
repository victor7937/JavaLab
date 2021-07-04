package com.epam.esm.repository.impl;

import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.criteria.SortingOrder;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.*;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.IncorrectPageRepositoryException;
import com.epam.esm.exception.PartialUpdateException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.CriteriaUtil;
import com.epam.esm.util.PartialUpdater;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.*;


@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final EntityManager entityManager;

    private final TagRepository tagRepository;

    @Autowired
    public GiftCertificateRepositoryImpl(EntityManager entityManager, TagRepository tagRepository) {
        this.entityManager = entityManager;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public PagedDTO<GiftCertificate> getByCriteria(CertificateCriteria certificateCriteria, int pageSize, int pageNumber) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> gcRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate conditions = criteriaBuilder.conjunction();
        criteriaQuery.select(gcRoot).distinct(true);

        if (certificateCriteria.isTagAdded()){
            conditions = criteriaBuilder.and(conditions, createTagsMatchingPredicate(certificateCriteria, gcRoot));
        }
        if (!certificateCriteria.getNamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, createNamePartPredicate(certificateCriteria, gcRoot));
        }
        if (!certificateCriteria.getDescriptionPart().isBlank()){
            conditions = criteriaBuilder.and(conditions, createDescriptionPartPredicate(certificateCriteria, gcRoot));
        }
        conditions = criteriaBuilder.and(conditions, createPriceBetweenPredicate(certificateCriteria, gcRoot));
        conditions = criteriaBuilder.and(conditions, createDateBetweenPredicate(certificateCriteria, gcRoot));

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, GiftCertificate.class);
        if (count == 0L){
            throw new DataNotExistRepositoryException();
        }

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);

        Path<?> sortPath = gcRoot.get(certificateCriteria.getField().attribute);
        if (certificateCriteria.getOrder() == SortingOrder.DESC) {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortPath));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortPath));
        }

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(criteriaQuery);

        List<GiftCertificate> resultList = typedQuery.setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

       return new PagedDTO<>(resultList, metadata);
    }

    @Override
    @Transactional
    public GiftCertificate getById(Long id) throws RepositoryException {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id)).orElseThrow(DataNotExistRepositoryException::new);
    }

    @Override
    @Transactional
    public GiftCertificate add(CertificateDTO certificateDTO) throws RepositoryException {
        GiftCertificate giftCertificate = new GiftCertificate(certificateDTO.getName(), certificateDTO.getDescription(),
                certificateDTO.getPrice(), certificateDTO.getDuration());
        Set<Tag> tagsForAdding = certificateDTO.getTags();
        tagsForAdding.stream().filter(t -> !tagRepository.isTagExists(t.getName())).forEach(giftCertificate::addTag);

        entityManager.persist(giftCertificate);
        tagsForAdding.stream().filter(t -> !giftCertificate.getTags().contains(t)).map(t -> tagRepository.getByName(t.getName()))
                .forEach(giftCertificate::addTag);

        return giftCertificate;
    }

    @Override
    @Transactional
    public void delete(Long id) throws RepositoryException {
        GiftCertificate giftCertificate = getById(id);
        entityManager.remove(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate update(CertificateDTO modified, Long id) throws RepositoryException {
        GiftCertificate current = getById(id);
        PartialUpdater<GiftCertificate, CertificateDTO> partialUpdater = new PartialUpdater<>(current, modified,
                List.of(GiftCertificate_.NAME, GiftCertificate_.DESCRIPTION, GiftCertificate_.PRICE, GiftCertificate_.DURATION));
        try {
            partialUpdater.generatePartialUpdateData();
        } catch (PartialUpdateException e) {
            throw new RepositoryException(e);
        }

        modified.getTags().stream().filter(tag -> !current.getTags().contains(tag))
                .map(t -> tagRepository.isTagExists(t.getName()) ? tagRepository.getByName(t.getName()) : t)
                .forEach(current::addTag);
        current.getTags().retainAll(modified.getTags());

        return current;
    }

    private Predicate createTagsMatchingPredicate(CertificateCriteria criteria, Root<GiftCertificate> gcRoot) throws DataNotExistRepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Expression<Set<Tag>> tags = gcRoot.get(GiftCertificate_.tags);
        Predicate tagsPredicate = criteriaBuilder.conjunction();
        for (String name : criteria.getTagNames()){
            Tag tag = tagRepository.getByName(name);
            if (tag == null){
                throw new DataNotExistRepositoryException();
            }
            tagsPredicate = criteriaBuilder.and(tagsPredicate, criteriaBuilder.isMember(tag, tags));
        }
        return tagsPredicate;
    }

    private Predicate createNamePartPredicate(CertificateCriteria criteria, Root<GiftCertificate> gcRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.like(gcRoot.get(GiftCertificate_.name),"%" + criteria.getNamePart() + "%");
    }

    private Predicate createDescriptionPartPredicate(CertificateCriteria criteria, Root<GiftCertificate> gcRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        return criteriaBuilder.like(gcRoot.get(GiftCertificate_.description),"%" + criteria.getDescriptionPart() + "%");
    }

    private Predicate createPriceBetweenPredicate(CertificateCriteria criteria, Root<GiftCertificate> gcRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate conjunction = criteriaBuilder.conjunction();
        conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.ge(gcRoot.get(GiftCertificate_.price), criteria.getMinPrice()));
        conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.le(gcRoot.get(GiftCertificate_.price), criteria.getMaxPrice()));
        return conjunction;
    }

    private Predicate createDateBetweenPredicate(CertificateCriteria criteria, Root<GiftCertificate> gcRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate conjunction = criteriaBuilder.conjunction();
        conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.greaterThanOrEqualTo(gcRoot.get(GiftCertificate_.createDate),
                criteria.getMinCreateDate()));
        conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.lessThanOrEqualTo(gcRoot.get(GiftCertificate_.createDate),
                criteria.getMaxCreateDate()));
        return conjunction;
    }





}
