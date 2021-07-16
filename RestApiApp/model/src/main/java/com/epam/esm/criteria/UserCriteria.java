package com.epam.esm.criteria;


import com.epam.esm.entity.User;
import com.epam.esm.entity.User_;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class UserCriteria extends Criteria {

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

    @Override
    public Map<String, String> getCriteriaAsMap(){
        Map<String, String> paramsMap = new LinkedHashMap<>();
        if (!namePart.isBlank()){
            paramsMap.put(RequestParams.NAME.value, namePart);
        }
        if (!surnamePart.isBlank()){
            paramsMap.put( RequestParams.SURNAME.value, surnamePart);
        }
        paramsMap.put(RequestParams.ORDER.value, sortingOrder.toString().toLowerCase());
        paramsMap.put(RequestParams.SORT.value, sortingField.toString().toLowerCase());
        return paramsMap;
    }

}
