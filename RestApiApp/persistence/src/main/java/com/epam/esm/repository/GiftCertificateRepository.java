package com.epam.esm.repository;

import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAll();
    List<GiftCertificate> getByCriteria(Criteria criteria);
    GiftCertificate getById(int id) throws RepositoryException;
    GiftCertificate add(GiftCertificate giftCertificate) throws RepositoryException;
    void delete(int id) throws RepositoryException;
    GiftCertificate update(GiftCertificate current, GiftCertificate modified) throws RepositoryException;
}
