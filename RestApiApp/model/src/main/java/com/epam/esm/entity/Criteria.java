package com.epam.esm.entity;

import org.apache.commons.lang3.EnumUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Criteria for searching gift certificates
 */
public class Criteria {

    private Set<String> tagNames;

    private String namePart;

    private SortingField field;

    private SortingOrder order;

    private boolean tagAdded;

    private Criteria(Optional<Set<String>> tagNames, Optional<String> namePart, Optional<Criteria.SortingField> field, Optional<SortingOrder> order) {
        this.tagAdded = true;
        this.tagNames = tagNames.orElseGet(() -> {
            tagAdded = false;
            return null;
        });
        this.namePart = namePart.orElse("");
        this.field = field.orElse(SortingField.ID);
        this.order = order.orElse(SortingOrder.ASC);
    }

    public static Criteria createCriteria(Optional<Set<String>> tagNames, Optional<String> namePart, Optional<String> fieldStr, Optional<String> orderStr) {
        Optional<Criteria.SortingField> field;
        Optional<SortingOrder> order;
        field = fieldStr.filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> Criteria.SortingField.valueOf(s.toUpperCase()));
        order = orderStr.filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        return new Criteria(tagNames, namePart, field, order);
    }

    public boolean isTagAdded() {
        return tagAdded;
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

    @Override
    public String toString() {
        return "Criteria{" +
                "tagNames=" + tagNames +
                ", namePart='" + namePart + '\'' +
                ", field=" + field +
                ", order=" + order +
                ", tagAdded=" + tagAdded +
                '}';
    }
}
