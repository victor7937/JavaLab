package com.epam.esm.repository;

import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.IncorrectPageRepositoryException;
import com.epam.esm.exception.RepositoryException;

public interface PagingAndFilteringCertificateRepository {

    /**
     * Get gift certificates with some criteria
     * @param criteria - searching certificates criteria
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with certificates which match the criteria
     */
    PagedDTO<GiftCertificate> getByCriteria(CertificateCriteria criteria, int pageSize, int pageNumber) throws IncorrectPageRepositoryException;
}
