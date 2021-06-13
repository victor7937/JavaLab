package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;


@Component
public class CertificateValidator implements ServiceValidator<GiftCertificate, Integer>{
    @Override
    public boolean validate(GiftCertificate entity) {
        return entity != null && entity.getName() != null && !entity.getName().isBlank() && entity.getPrice() != null
                && entity.getPrice() >= 0.0f && entity.getDuration() != null && entity.getDuration() > 0
                && entity.getTags() != null && !entity.getTags().contains(null)
                && entity.getTags().stream().noneMatch(t -> t.getName() == null || t.getName().isBlank());
    }

    @Override
    public boolean isIdValid(Integer id) {
        return id != null && id > 0;
    }

}
