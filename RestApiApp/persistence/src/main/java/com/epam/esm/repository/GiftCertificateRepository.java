package com.epam.esm.repository;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

/**
 * Repository for manipulating certificate data in certificates-tag database
 */
public interface GiftCertificateRepository {

    /**
     * Get gift certificates with some criteria
     * @param criteria - searching certificates criteria
     * @return list of some certificates which match the criteria
     */
    List<GiftCertificate> getByCriteria(Criteria criteria);

    /**
     * Get one gift certificate by id if such id exists
     * @param id - id of gift certificate
     * @return certificate found
     * @throws RepositoryException if such id exists or some troubles in database were happened
     */
    GiftCertificate getById(Long id) throws RepositoryException;

    /**
     * Add new gift certificate to database
     * @param giftCertificate - certificate for adding to database
     * @return Added gift certificate with new generated data
     * @throws RepositoryException if some troubles in database were happened
     */
    GiftCertificate add(CertificateDTO giftCertificate) throws RepositoryException;

    /**
     * Delete gift certificate from database
     * @param id - id of gift certificate for deleting
     * @throws RepositoryException if such id exists or some troubles in database were happened
     */
    void delete(Long id) throws RepositoryException;

    /**
     * @param modified - gift certificate after modifying
     * @return modified gift certificate with some generated data
     * @throws RepositoryException if some troubles in database were happened
     */
    GiftCertificate update(CertificateDTO modified, Long id) throws RepositoryException;
}
