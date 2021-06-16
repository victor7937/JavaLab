package com.epam.esm.entity;

import org.apache.commons.lang3.EnumUtils;

import java.util.Optional;

/**
 * Criteria for searching by tag
 */
public class Criteria {

    private String tagName;

    private SortingField field;

    private SortingOrder order;

    private boolean tagAdded;

    private Criteria(Optional<String> tagName, Optional<Criteria.SortingField> field, Optional<SortingOrder> order) {
        this.tagAdded = true;
        this.tagName = tagName.orElseGet(() -> {
            tagAdded = false;
            return null;
        });
        this.field = field.orElse(SortingField.ID);
        this.order = order.orElse(SortingOrder.ASC);
    }

    public static Criteria createCriteria(Optional<String> tagName, Optional<String> fieldStr, Optional<String> orderStr) {
        Optional<Criteria.SortingField> field;
        Optional<SortingOrder> order;
        field = fieldStr.filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> Criteria.SortingField.valueOf(s.toUpperCase()));
        order = orderStr.filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        return new Criteria(tagName, field, order);
    }

    public boolean isTagAdded() {
        return tagAdded;
    }

    public String getTagName() {
        return tagName;
    }

    public SortingField getField() {
        return field;
    }

    public SortingOrder getOrder() {
        return order;
    }



    public static enum SortingField {
        NAME, CREATE_DATE, ID, PRICE, DURATION, LAST_UPDATE_DATE
    }

    public static enum SortingOrder {
        ASC, DESC
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "tagName='" + tagName + '\'' +
                ", field=" + field +
                ", type=" + order +
                ", tagAdded=" + tagAdded +
                '}';
    }
}
