package com.epam.esm.repository.impl;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.PartialUpdateException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.PartialUpdater;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
    public List<GiftCertificate> getByCriteria(Criteria criteria) {
        return entityManager.createQuery(JPQL_GET_ALL, GiftCertificate.class).getResultList();
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
