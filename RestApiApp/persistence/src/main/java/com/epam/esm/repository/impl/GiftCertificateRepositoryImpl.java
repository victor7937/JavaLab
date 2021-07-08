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
    public PagedDTO<GiftCertificate> getByCriteria(CertificateCriteria criteria, int pageSize, int pageNumber) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> gcRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate conditions = criteriaBuilder.conjunction();
        criteriaQuery.select(gcRoot).distinct(true);

        if (criteria.isTagAdded()){
            boolean areAllTagsValid = true;
            try {
                conditions = criteriaBuilder.and(conditions, createTagsMatchingPredicate(criteria, gcRoot));
            } catch (DataNotExistRepositoryException e){
                areAllTagsValid = false;
            }
            if (!areAllTagsValid) {
                return new PagedDTO<>();
            }
        }

        conditions = criteriaBuilder.and(conditions, createPredicates(criteria, gcRoot));

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, GiftCertificate.class);
        if (count == 0L){
            return new PagedDTO<>();
        }

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);

        CriteriaUtil.applySortingParams(entityManager, gcRoot, criteriaQuery, criteria.getField().attribute,
                criteria.getOrder());

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

    private Predicate createPredicates(CertificateCriteria criteria, Root<GiftCertificate> gcRoot){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate conditions = criteriaBuilder.conjunction();
        if (!criteria.getNamePart().isBlank()){
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.like(gcRoot.get(GiftCertificate_.name),"%"
                    + criteria.getNamePart() + "%"));
        }
        if (!criteria.getDescriptionPart().isBlank()){
            conditions = criteriaBuilder.and(conditions, criteriaBuilder.like(gcRoot.get(GiftCertificate_.description),"%"
                    + criteria.getDescriptionPart() + "%"));
        }
        conditions = criteriaBuilder.and(conditions, criteriaBuilder.between(gcRoot.get(GiftCertificate_.price),
                criteria.getMinPrice(), criteria.getMaxPrice()));
        conditions = criteriaBuilder.and(conditions,  criteriaBuilder.between(gcRoot.get(GiftCertificate_.createDate),
                criteria.getMinCreateDate(), criteria.getMaxCreateDate()));

        return conditions;
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

}
