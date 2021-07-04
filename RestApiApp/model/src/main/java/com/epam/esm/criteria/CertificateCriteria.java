package com.epam.esm.criteria;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import org.apache.commons.lang3.EnumUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Criteria for searching gift certificates
 */
public class CertificateCriteria {

    private Set<String> tagNames;

    private String namePart;

    private SortingField field;

    private SortingOrder order;


    private CertificateCriteria(Optional<Set<String>> tagNames, Optional<String> namePart, Optional<CertificateCriteria.SortingField> field, Optional<SortingOrder> order) {
        this.tagNames = tagNames.orElse(new HashSet<>());
        this.namePart = namePart.orElse("");
        this.field = field.orElse(SortingField.ID);
        this.order = order.orElse(SortingOrder.ASC);
    }

    public static CertificateCriteria createCriteria(Map<String, String> criteriaParams) {
        Optional<CertificateCriteria.SortingField> field = Optional.ofNullable(criteriaParams.get(RequestParams.SORT.value))
                .filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> CertificateCriteria.SortingField.valueOf(s.toUpperCase()));
        Optional<SortingOrder> order = Optional.ofNullable(criteriaParams.get(RequestParams.ORDER.value))
                .filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));
        Optional<String> namePart = Optional.ofNullable(criteriaParams.get(RequestParams.NAME_PART.value));
        Optional<Set<String>> tagNames = Optional.ofNullable(criteriaParams.get(RequestParams.TAGS.value))
                .map(s -> Set.of(s.split(",")));


        return new CertificateCriteria(tagNames, namePart, field, order);
    }

    public boolean isTagAdded() {
        return !tagNames.isEmpty();
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public String getNamePart() {
        return namePart;
    }

    public SortingField getField() {
        return field;
    }

    public SortingOrder getOrder() {
        return order;
    }

    public enum SortingField {

        NAME(GiftCertificate_.name), CREATE_DATE(GiftCertificate_.createDate), ID(GiftCertificate_.id), PRICE(GiftCertificate_.price),
        DURATION(GiftCertificate_.duration), LAST_UPDATE_DATE(GiftCertificate_.lastUpdateDate);

        public final SingularAttribute<GiftCertificate, ?> attribute;

        SortingField(SingularAttribute<GiftCertificate, ?> attribute) {
            this.attribute = attribute;
        }
    }

    public enum SortingOrder {
        ASC, DESC
    }

    public enum RequestParams{
        SORT("sort"), NAME_PART("name_part"), DESCRIPTION_PART("description_part"), ORDER("order"), TAGS("tags"),
        PRICE("price"), PRICE_GT("price[gt]"), PRICE_LT("price[lt]");

        public final String value;

        RequestParams(String value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "CertificateCriteria{" +
                "tagNames=" + tagNames +
                ", namePart='" + namePart + '\'' +
                ", field=" + field +
                ", order=" + order +
                '}';
    }
}
