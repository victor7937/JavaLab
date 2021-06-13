package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    List<GiftCertificate> get (Optional<String> tagName);
    GiftCertificate getById(int id) throws ServiceException;
    GiftCertificate add(GiftCertificate giftCertificate) throws ServiceException;
    void delete(int id) throws ServiceException;
    GiftCertificate update(GiftCertificate current, GiftCertificate modified) throws ServiceException;
}
