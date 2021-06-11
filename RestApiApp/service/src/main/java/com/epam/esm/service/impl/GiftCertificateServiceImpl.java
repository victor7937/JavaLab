package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.NotFoundServiceException;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;

    private static final String NOT_EXIST_MSG = "Gift Certificate id with number %s doesn't exist";

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateRepository.getAll();
    }

    @Override
    public GiftCertificate getById(int id) throws ServiceException{
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
    public void add(GiftCertificate giftCertificate) throws ServiceException {
        try {
            giftCertificateRepository.add(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try {
            giftCertificateRepository.delete(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(GiftCertificate current, GiftCertificate modified) throws ServiceException {
        try {
            giftCertificateRepository.update(current, modified);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
