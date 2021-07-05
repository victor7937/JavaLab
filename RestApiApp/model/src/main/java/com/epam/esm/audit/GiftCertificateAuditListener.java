package com.epam.esm.audit;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class GiftCertificateAuditListener {

    @PrePersist
    public void setDateTimeBeforeCreating(GiftCertificate giftCertificate){
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @PreUpdate
    public void setDateTimeBeforeUpdating(GiftCertificate giftCertificate){
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }
}