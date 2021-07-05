package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class CertificateValidator implements ServiceValidator<CertificateDTO, Long>{
    @Override
    public boolean validate(CertificateDTO entity) {
        return entity != null && entity.getName() != null && !entity.getName().isBlank() && entity.getPrice() != null
                && entity.getPrice() >= 0.0f && entity.getDuration() != null && entity.getDuration() > 0
                && entity.getTags() != null && entity.getTags().stream().noneMatch(Objects::isNull)
                && entity.getTags().stream().noneMatch(t -> t.getName() == null || t.getName().isBlank());
    }

    @Override
    public boolean isIdValid(Long id) {
        return id != null && id > 0;
    }

}
