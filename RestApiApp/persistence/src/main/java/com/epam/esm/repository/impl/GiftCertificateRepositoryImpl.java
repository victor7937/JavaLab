package com.epam.esm.repository.impl;

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

    private static final String JPQL_GET_ALL = "SELECT g from GiftCertificate g";

    private final EntityManager entityManager;

    private final TagRepository tagRepository;

    @Autowired
    public GiftCertificateRepositoryImpl(EntityManager entityManager, TagRepository tagRepository) {
        this.entityManager = entityManager;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public PagedDTO<GiftCertificate> getByCriteria(Criteria criteria, int pageSize, int pageNumber) throws RepositoryException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);

        Root<GiftCertificate> gcRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate conditions = criteriaBuilder.conjunction();
        criteriaQuery.select(gcRoot).distinct(true);

        if (criteria.isTagAdded()){
            Expression<Set<Tag>> tags = gcRoot.get(GiftCertificate_.tags);
            for (String name : criteria.getTagNames()){
                Tag tag = tagRepository.getByName(name);
                if (tag == null){
                    return new PagedDTO<>();
                }
                Predicate p = criteriaBuilder.isMember(tag, tags);
                conditions = criteriaBuilder.and(conditions, p);
            }
        }
        if (!criteria.getNamePart().isBlank()){
            Predicate p = criteriaBuilder.like(gcRoot.get(GiftCertificate_.name),"%" + criteria.getNamePart() + "%");
            conditions = criteriaBuilder.and(conditions, p);
        }

        Long count = CriteriaUtil.getResultsCount(entityManager, conditions, GiftCertificate.class);
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(pageSize, pageNumber, count);
        if (metadata.getTotalPages() < metadata.getNumber()) {
            throw new IncorrectPageRepositoryException();
        }

        criteriaQuery.where(conditions);

        Path<?> sortPath = gcRoot.get(criteria.getField().attribute);
        if (criteria.getOrder() == Criteria.SortingOrder.DESC) {
            criteriaQuery.orderBy(criteriaBuilder.desc(sortPath));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(sortPath));
        }

        TypedQuery<GiftCertificate> typedQuery = entityManager.createQuery(criteriaQuery);

        System.out.println(metadata.getTotalPages());
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



}
