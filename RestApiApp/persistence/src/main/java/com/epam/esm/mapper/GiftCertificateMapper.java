package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    private static final String CERTIFICATE_ID = "id";
    private static final String CERTIFICATE_NAME = "name";
    private static final String CERTIFICATE_DESCRIPTION = "description";
    private static final String CERTIFICATE_PRICE = "price";
    private static final String CERTIFICATE_DURATION = "duration";
    private static final String CERTIFICATE_CREATE_DATE = "create_date";
    private static final String CERTIFICATE_LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(resultSet.getInt(CERTIFICATE_ID));
        giftCertificate.setName(resultSet.getString(CERTIFICATE_NAME));
        giftCertificate.setDescription(resultSet.getString(CERTIFICATE_DESCRIPTION));
        giftCertificate.setPrice(resultSet.getFloat(CERTIFICATE_PRICE));
        giftCertificate.setDuration(resultSet.getInt(CERTIFICATE_DURATION));
        giftCertificate.setCreateDate(resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime());

        return giftCertificate;
    }
}
