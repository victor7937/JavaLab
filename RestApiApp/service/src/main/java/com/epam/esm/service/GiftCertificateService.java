package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;


/**
 * Service for manipulating gift certificates data
 */
public interface GiftCertificateService {


    /**
     * Gets page with gift certificates from data source
     * @param certificateCriteria - criteria with params for filtering and sorting
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with certificates found
     * @throws ServiceException if criteria or pagination params are incorrect or some troubles in data source were happened
     */
    PagedDTO<GiftCertificate> get (CertificateCriteria certificateCriteria, int pageSize, int pageNumber) throws ServiceException;

    /**
     * Get one gift certificate if id is correct
     * @param id - id of gift certificate
     * @return certificate found
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    GiftCertificate getById(Long id) throws ServiceException;

    /**
     * Add new gift certificate
     * @param giftCertificate - certificate dto with necessary fields for adding to data source
     * @return Added gift certificate with new generated data
     * @throws ServiceException params is incorrect or some troubles in data source were happened
     */
    GiftCertificate add(CertificateDTO giftCertificate) throws ServiceException;

    /**
     * Delete gift certificate
     * @param id - id of gift certificate for deleting
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    void delete(Long id) throws ServiceException;

    /**
     * @param modified - gift certificate dto that contains modified fields
     * @param id - id of certificate for modifying
     * @return modified gift certificate with some generated data
     * @throws ServiceException if modified dto contains incorrect data or id is incorrect
     */
    GiftCertificate update(CertificateDTO modified, Long id) throws ServiceException;


}
