package com.epam.esm.criteria;


import com.epam.esm.entity.User;
import com.epam.esm.entity.User_;
import org.apache.commons.lang3.EnumUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Map;
import java.util.Optional;

public class UserCriteria {

    private String namePart;

    private String surnamePart;

    private SortingField sortingField;

    private SortingOrder sortingOrder;

    public UserCriteria(Optional<SortingField> sortingField, Optional<SortingOrder> sortingOrder, Optional<String> namePart,
                        Optional<String> surnamePart ){
        this.sortingField = sortingField.orElse(SortingField.EMAIL);
        this.sortingOrder = sortingOrder.orElse(SortingOrder.ASC);
        this.namePart = namePart.orElse("");
        this.surnamePart = surnamePart.orElse("");
    }

    public static UserCriteria createCriteria(Map<String, String> criteriaParams) {
        Optional<SortingField> sortingField = Optional.ofNullable(criteriaParams.get(RequestParams.SORT.value))
                .filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> SortingField.valueOf(s.toUpperCase()));

        Optional<SortingOrder> order = Optional.ofNullable(criteriaParams.get(RequestParams.ORDER.value))
                .filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        Optional<String> namePart = Optional.ofNullable(criteriaParams.get(RequestParams.NAME.value));
        Optional<String> surnamePart = Optional.ofNullable(criteriaParams.get(RequestParams.SURNAME.value));

        return new UserCriteria(sortingField, order, namePart, surnamePart);
    }

    public String getNamePart() {
        return namePart;
    }

    public String getSurnamePart() {
        return surnamePart;
    }

    public SortingField getSortingField() {
        return sortingField;
    }

    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }

    public enum SortingField {

        NAME(User_.name), SURNAME(User_.surname), EMAIL(User_.email);

        public final SingularAttribute<User, ?> attribute;

        SortingField(SingularAttribute<User, ?> attribute) {
            this.attribute = attribute;
        }
    }

    public enum RequestParams{
        NAME("name"), SURNAME("surname"), SORT("sort"), ORDER("order");

        public final String value;

        RequestParams(String value) {
            this.value = value;
        }
    }

}
