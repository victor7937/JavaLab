package com.epam.esm.service.impl;

import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.ServiceValidator;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {


    private static final String INCORRECT_CERTIFICATE_MSG = "Incorrect certificate data";
    private final GiftCertificateRepository giftCertificateRepository;

    private final ServiceValidator<GiftCertificate, Integer> validator;

    private static final String INVALID_ID_MSG = "Certificate id is invalid";
    private static final String NOT_EXIST_MSG = "Gift Certificate id with number %s doesn't exist";

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, ServiceValidator<GiftCertificate, Integer> validator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.validator = validator;
    }

    @Override
    public List<GiftCertificate> get(Optional<String> tagName, Optional<String> sortBy, Optional<String> sortOrder) {
        return giftCertificateRepository.getByCriteria(Criteria.createCriteria(tagName, sortBy, sortOrder));
    }

    @Override
    public GiftCertificate getById(Integer id) throws ServiceException{
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
    public GiftCertificate add(GiftCertificate giftCertificate) throws ServiceException {
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
    public void delete(Integer id) throws ServiceException {
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
    public GiftCertificate update(GiftCertificate current, GiftCertificate modified) throws ServiceException {
        if (!(validator.validate(current) && validator.validate(modified))){
            throw new IncorrectDataServiceException(INCORRECT_CERTIFICATE_MSG);
        }

        GiftCertificate resultCertificate;
        try {
            resultCertificate = giftCertificateRepository.update(current, modified);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return resultCertificate;
    }

}
