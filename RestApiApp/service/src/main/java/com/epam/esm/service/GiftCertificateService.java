package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;


/**
 * Service for manipulating gift certificates data
 */
public interface GiftCertificateService {

    /**
     * Get all gift certificates or some of them by params
     * @param tagName - tag for searching
     * @param sortBy - field name for sorting
     * @param sortOrder - sorting order
     * @param namePart - part of certificate name for searching
     * @return list of certificates found
     */
    List<GiftCertificate> get (Optional<String> tagName, Optional<String> namePart, Optional<String> sortBy, Optional<String> sortOrder);

    /**
     * Get one gift certificate if id is correct
     * @param id - id of gift certificate
     * @return certificate found
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    GiftCertificate getById(Integer id) throws ServiceException;

    /**
     * Add new gift certificate
     * @param giftCertificate - certificate for adding to data source
     * @return Added gift certificate with new generated data
     * @throws ServiceException params is incorrect or some troubles in data source were happened
     */
    GiftCertificate add(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Delete gift certificate
     * @param id - id of gift certificate for deleting
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    void delete(Integer id) throws ServiceException;

    /**
     * @param current - gift certificate before modifying
     * @param modified - gift certificate after modifying
     * @return modified gift certificate with some generated data
     * @throws ServiceException if current or modified contains incorrect data
     */
    GiftCertificate update(GiftCertificate current, GiftCertificate modified) throws ServiceException;
}
