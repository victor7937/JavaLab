package com.epam.esm.criteria;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Criteria for searching gift certificates
 */
public class CertificateCriteria {

    private static final String MIN_DATE_TIME = "2021-06-01T00:00:00";

    private Set<String> tagNames;

    private String namePart;

    private String descriptionPart;

    private SortingField field;

    private SortingOrder order;

    private Float minPrice;

    private Float maxPrice;

    private LocalDateTime minCreateDate;

    private LocalDateTime maxCreateDate;




    private CertificateCriteria(Optional<Set<String>> tagNames, Optional<String> namePart, Optional<String> descriptionPart,
                                Optional<CertificateCriteria.SortingField> field, Optional<SortingOrder> order, Optional<Float> minPrice,
                                Optional<Float> maxPrice, Optional<LocalDateTime> minDate, Optional<LocalDateTime> maxDate) {
        this.tagNames = tagNames.orElse(new HashSet<>());
        this.namePart = namePart.orElse("");
        this.descriptionPart = descriptionPart.orElse("");
        this.field = field.orElse(SortingField.ID);
        this.order = order.orElse(SortingOrder.ASC);
        this.minPrice = minPrice.orElse(0.0f);
        this.maxPrice = maxPrice.orElse(Float.MAX_VALUE);
        this.minCreateDate = minDate.orElse(LocalDateTime.parse(MIN_DATE_TIME));
        this.maxCreateDate = maxDate.orElse(LocalDateTime.now());
    }

    public static CertificateCriteria createCriteria(Map<String, String> criteriaParams) {
        Optional<CertificateCriteria.SortingField> field = Optional.ofNullable(criteriaParams.get(RequestParams.SORT.value))
                .filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> CertificateCriteria.SortingField.valueOf(s.toUpperCase()));

        Optional<SortingOrder> order = Optional.ofNullable(criteriaParams.get(RequestParams.ORDER.value))
                .filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        Optional<Set<String>> tagNames = Optional.ofNullable(criteriaParams.get(RequestParams.TAGS.value))
                .map(s -> Set.of(s.split(",")));

        Optional<String> namePart = Optional.ofNullable(criteriaParams.get(RequestParams.NAME_PART.value));
        Optional<String> descriptionPart = Optional.ofNullable(criteriaParams.get(RequestParams.DESCRIPTION_PART.value));

        Optional<Float> minPrice = Optional.ofNullable(criteriaParams.get(RequestParams.PRICE_GTE.value))
                .filter(NumberUtils::isCreatable)
                .map(Float::parseFloat);

        Optional<Float> maxPrice = Optional.ofNullable(criteriaParams.get(RequestParams.PRICE_LTE.value))
                .filter(NumberUtils::isCreatable)
                .map(Float::parseFloat);

        Optional<LocalDateTime> minDate;
        try {
            minDate = Optional.ofNullable(criteriaParams.get(RequestParams.CREATE_GTE.value)).map(LocalDateTime::parse);
        } catch (DateTimeParseException e){
            minDate = Optional.empty();
        }

        Optional<LocalDateTime> maxDate;
        try {
            maxDate = Optional.ofNullable(criteriaParams.get(RequestParams.CREATE_LTE.value)).map(LocalDateTime::parse);
        } catch (DateTimeParseException e){
            maxDate = Optional.empty();
        }

        return new CertificateCriteria(tagNames, namePart, descriptionPart, field, order, minPrice, maxPrice, minDate, maxDate);
    }

    public boolean isTagAdded() {
        return !tagNames.isEmpty();
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    public String getNamePart() {
        return namePart;
    }

    public LocalDateTime getMinCreateDate() {
        return minCreateDate;
    }

    public LocalDateTime getMaxCreateDate() {
        return maxCreateDate;
    }

    public String getDescriptionPart() {
        return descriptionPart;
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

    public enum RequestParams{
        SORT("sort"), NAME_PART("name_part"), DESCRIPTION_PART("description_part"), ORDER("order"),
        TAGS("tags"), PRICE_GTE("price-gte"), PRICE_LTE("price-lte"), CREATE_GTE("create_date-gte"),
        CREATE_LTE("create_date-lte");

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
