package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    List<GiftCertificate> getAll();
    GiftCertificate getById(int id);
}
