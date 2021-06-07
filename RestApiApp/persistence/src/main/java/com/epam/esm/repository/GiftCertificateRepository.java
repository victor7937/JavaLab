package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface GiftCertificateRepository {
    List<GiftCertificate> getAll();
    GiftCertificate getById(int id) throws RepositoryException;
}
