package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Service for manipulating gift certificates data
 */
public interface GiftCertificateService {


    PagedDTO<GiftCertificate> get (Criteria criteria, int pageSize, int pageNumber) throws ServiceException;

    /**
     * Get one gift certificate if id is correct
     * @param id - id of gift certificate
     * @return certificate found
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    GiftCertificate getById(Long id) throws ServiceException;

    /**
     * Add new gift certificate
     * @param giftCertificate - certificate for adding to data source
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
     * @param modified - gift certificate after modifying
     * @return modified gift certificate with some generated data
     * @throws ServiceException if current or modified contains incorrect data
     */
    GiftCertificate update(CertificateDTO modified, Long id) throws ServiceException;
}
