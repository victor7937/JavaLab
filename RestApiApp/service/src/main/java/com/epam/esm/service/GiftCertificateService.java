package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();
    GiftCertificate getById(int id) throws ServiceException;
    void add(GiftCertificate giftCertificate) throws ServiceException;
    void delete(int id) throws ServiceException;
    void update(GiftCertificate current, GiftCertificate modified) throws ServiceException;
}
