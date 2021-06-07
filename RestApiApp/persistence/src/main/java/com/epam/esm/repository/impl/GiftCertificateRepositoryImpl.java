package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SQL_GET_ALL = "SELECT * FROM gift_certificate";
    private static final String SQL_GET_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new GiftCertificateMapper());
    }

    @Override
    public GiftCertificate getById(int id) throws RepositoryException {
        return jdbcTemplate.query(SQL_GET_BY_ID, new GiftCertificateMapper(), id)
                .stream()
                .findAny()
                .orElseThrow(DataNotExistRepositoryException::new);
    }
}
