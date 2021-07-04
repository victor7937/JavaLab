package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;

    private final ServiceValidator<CertificateDTO, Long> validator;

    private static final String INVALID_ID_MSG = "Certificate id is invalid";
    private static final String NOT_EXIST_MSG = "Gift Certificate id with number %s doesn't exist";
    private static final String NO_SUCH_PAGE_MSG = "Page with number %s doesn't exist";
    private static final String INCORRECT_CERTIFICATE_MSG = "Incorrect certificate data";
    private static final String INVALID_PAGE_PARAMS = "Page params are invalid";
    private static final String NOT_EXIST_WITH_CRITERIA_MSG = "No gift certificates with such criteria";

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, ServiceValidator<CertificateDTO, Long> validator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.validator = validator;
    }

    @Override
    public PagedDTO<GiftCertificate> get(CertificateCriteria certificateCriteria, int pageSize, int pageNumber) throws ServiceException {
        PagedDTO<GiftCertificate> pagedDTO;
        if (!validator.isPageParamsValid(pageSize, pageNumber)){
            throw new IncorrectDataServiceException(INVALID_PAGE_PARAMS);
        }
        try {
            pagedDTO = giftCertificateRepository.getByCriteria(certificateCriteria, pageSize, pageNumber);
        } catch (IncorrectPageRepositoryException e) {
            throw new IncorrectPageServiceException(String.format(NO_SUCH_PAGE_MSG, pageNumber), e);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(NOT_EXIST_WITH_CRITERIA_MSG, e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return pagedDTO;
    }

    @Override
    public GiftCertificate getById(Long id) throws ServiceException{
        if (!validator.isIdValid(id)){
            throw new IncorrectDataServiceException(INVALID_ID_MSG);
        }

        GiftCertificate giftCertificate;
        try {
            giftCertificate = giftCertificateRepository.getById(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return giftCertificate;
    }

    @Override
    public GiftCertificate add(CertificateDTO giftCertificate) throws ServiceException {
        if (!validator.validate(giftCertificate)){
            throw new IncorrectDataServiceException(INCORRECT_CERTIFICATE_MSG);
        }

        GiftCertificate resultCertificate;
        try {
           resultCertificate = giftCertificateRepository.add(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return resultCertificate;
    }

    @Override
    public void delete(Long id) throws ServiceException {
        if (!validator.isIdValid(id)){
            throw new IncorrectDataServiceException(INVALID_ID_MSG);
        }

        try {
            giftCertificateRepository.delete(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public GiftCertificate update(CertificateDTO modified, Long id) throws ServiceException {
        if (!(validator.validate(modified) && validator.isIdValid(id))){
            throw new IncorrectDataServiceException(INCORRECT_CERTIFICATE_MSG);
        }

        GiftCertificate resultCertificate;
        try {
            resultCertificate = giftCertificateRepository.update(modified, id);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return resultCertificate;
    }

}
