package com.epam.esm.repository.impl;

import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.PartialUpdateException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.PartialUpdateQueryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;


@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SQL_GET_ALL = "SELECT * FROM gift_certificate ORDER BY %s %s";
    private static final String SQL_GET_ALL_WITH_TAG_NAME = "SELECT g.id, g.name, g.description, g.price, g.duration," +
            " g.create_date, g.last_update_date FROM gift_certificate g" +
    " JOIN m2m_certificate_tag m2mct ON g.id = m2mct.cert_id" +
    " JOIN tag t ON t.id = m2mct.tag_id WHERE t.name = ? ORDER BY g.%s %s";

    private static final String SQL_GET_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?";

    private static final String SQL_GET_TAGS_FOR_CERTIFICATE = "SELECT t.name, t.id FROM gift_certificate g" +
    " JOIN m2m_certificate_tag m2mct ON g.id = m2mct.cert_id" +
    " JOIN tag t ON t.id = m2mct.tag_id WHERE g.id = ?";

    private static final String SQL_ADD_NEW_CERTIFICATE = "INSERT INTO gift_certificate(name, description, price, duration) VALUES(?,?,?,?)";

    private static final String SQL_TAG_EXISTS = "SELECT EXISTS (SELECT 1 FROM tag WHERE tag.name = ?) AS tag_exists";

    private static final String SQL_ADD_NEW_TAG = "INSERT INTO tag(name) VALUES(?)";

    private static final String SQL_GET_TAG_ID = "SELECT id FROM tag WHERE name = ?";

    private static final String SQL_ADD_TO_CERT_TAG_M2M = "INSERT INTO m2m_certificate_tag(cert_id, tag_id) VALUES(?,?)";

    private static final String SQL_DELETE_FROM_CERT_TAG_M2M = "DELETE FROM m2m_certificate_tag WHERE cert_id = ? AND tag_id = ?";

    private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";

    private static final String UPDATE_QUERY_CONDITION = " WHERE id = ?";

    private static final List<String> PARTIAL_UPDATE_FIELDS =  List.of("name", "description", "price", "duration");

    private static final String ADDING_CERTIFICATE_FAIL_MSG = "Adding certificate fail";
    private static final String CERTIFICATES_TABLE_NAME = "gift_certificate";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GiftCertificate> getByCriteria(Criteria criteria) {
        UnaryOperator<String> insertParams = s -> String.format(s, criteria.getField().toString().toLowerCase(),
                criteria.getOrder().toString());
        List<GiftCertificate> giftCertificateList;
        if (criteria.isTagAdded()) {
            giftCertificateList = jdbcTemplate.query(insertParams.apply(SQL_GET_ALL_WITH_TAG_NAME), new GiftCertificateMapper(), criteria.getTagName());
        } else {
            giftCertificateList = jdbcTemplate.query(insertParams.apply(SQL_GET_ALL), new GiftCertificateMapper());
        }
        giftCertificateList.forEach(cert -> cert.setTags(getTagsById(cert.getId())));
        return giftCertificateList;
    }

    @Override
    public GiftCertificate getById(int id) throws RepositoryException {
        GiftCertificate giftCertificate = jdbcTemplate.query(SQL_GET_BY_ID, new GiftCertificateMapper(), id).stream().findAny()
                .orElseThrow(DataNotExistRepositoryException::new);
        giftCertificate.setTags(getTagsById(giftCertificate.getId()));
        return giftCertificate;
    }

    @Override
    @Transactional("transactionManager")
    public GiftCertificate add(GiftCertificate giftCertificate) throws RepositoryException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        AtomicInteger i = new AtomicInteger();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_NEW_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(i.incrementAndGet(), giftCertificate.getName());
            ps.setString(i.incrementAndGet(), giftCertificate.getDescription());
            ps.setFloat(i.incrementAndGet(), giftCertificate.getPrice());
            ps.setInt(i.incrementAndGet(), giftCertificate.getDuration());
            return ps;
        }, keyHolder);

        int certificateId = (Integer) Optional.ofNullable(keyHolder.getKeys().get("id"))
                .orElseThrow(() -> new RepositoryException(ADDING_CERTIFICATE_FAIL_MSG));

        giftCertificate.getTags().forEach(tag -> connectTagWithCertificate(tag, certificateId));
        return getById(certificateId);
    }

    @Override
    public void delete(int id) throws RepositoryException {
        int rowsAffected = jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id);
        if (rowsAffected == 0){
            throw new DataNotExistRepositoryException();
        }
    }

    @Override
    @Transactional("transactionManager")
    public GiftCertificate update(GiftCertificate current, GiftCertificate modified) throws RepositoryException {
        try {
            PartialUpdateQueryCreator<GiftCertificate> queryCreator = new PartialUpdateQueryCreator<>(current, modified,
                    PARTIAL_UPDATE_FIELDS);
            queryCreator.generatePartialUpdateData();

            if (queryCreator.isUpdated()){
                String updateQuery = queryCreator.createQuery(CERTIFICATES_TABLE_NAME) + UPDATE_QUERY_CONDITION;
                List<Object> updatedValues = queryCreator.getValues();
                updatedValues.add(current.getId());
                jdbcTemplate.update(updateQuery, updatedValues.toArray());
            }

        } catch (PartialUpdateException e) {
            throw new RepositoryException(e);
        }

        modified.getTags().stream()
                .filter(tag -> !current.getTags().contains(tag))
                .forEach(tag -> connectTagWithCertificate(tag, current.getId()));

        current.getTags().stream()
                .filter(tag -> !modified.getTags().contains(tag))
                .forEach(tag -> unlinkTagFromCertificate(tag, current.getId()));

        return getById(current.getId());

    }

    private Set<Tag> getTagsById(int id){
        return new LinkedHashSet<>(jdbcTemplate.query(SQL_GET_TAGS_FOR_CERTIFICATE, new BeanPropertyRowMapper<>(Tag.class), id));
    }

    private boolean isTagExists(Tag tag) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_TAG_EXISTS, new SingleColumnRowMapper<>(Boolean.class),
                tag.getName())).orElse(false);
    }

    private Integer getTagIdByName(String tagName){
        return jdbcTemplate.queryForObject(SQL_GET_TAG_ID, new SingleColumnRowMapper<>(Integer.class), tagName);
    }

    private void connectTagWithCertificate(Tag tag, int certificateId){
        if (!isTagExists(tag)) {
            jdbcTemplate.update(SQL_ADD_NEW_TAG, tag.getName());
        }
        jdbcTemplate.update(SQL_ADD_TO_CERT_TAG_M2M, certificateId, getTagIdByName(tag.getName()));
    }

    private void unlinkTagFromCertificate(Tag tag, int certificateId){
        jdbcTemplate.update(SQL_DELETE_FROM_CERT_TAG_M2M, certificateId, getTagIdByName(tag.getName()));
    }


}
