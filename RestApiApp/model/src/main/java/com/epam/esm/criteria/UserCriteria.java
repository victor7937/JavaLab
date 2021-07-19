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

    private String firstNamePart;

    private String lastNamePart;

    private SortingField sortingField;

    private SortingOrder sortingOrder;

    public UserCriteria(Optional<SortingField> sortingField, Optional<SortingOrder> sortingOrder, Optional<String> firstNamePart,
                        Optional<String> lastNamePart){
        this.sortingField = sortingField.orElse(SortingField.ID);
        this.sortingOrder = sortingOrder.orElse(SortingOrder.ASC);
        this.firstNamePart = firstNamePart.orElse("");
        this.lastNamePart = lastNamePart.orElse("");
    }

    public static UserCriteria createCriteria(Map<String, String> criteriaParams) {
        Optional<SortingField> sortingField = Optional.ofNullable(criteriaParams.get(RequestParams.SORT.value))
                .filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> SortingField.valueOf(s.toUpperCase()));

        Optional<SortingOrder> order = Optional.ofNullable(criteriaParams.get(RequestParams.ORDER.value))
                .filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        Optional<String> namePart = Optional.ofNullable(criteriaParams.get(RequestParams.FIRST_NAME.value));
        Optional<String> surnamePart = Optional.ofNullable(criteriaParams.get(RequestParams.LAST_NAME.value));

        return new UserCriteria(sortingField, order, namePart, surnamePart);
    }

    public enum SortingField {

        NAME(User_.firstName), SURNAME(User_.lastName), EMAIL(User_.email), ID(User_.id);

        public final SingularAttribute<User, ?> attribute;

        SortingField(SingularAttribute<User, ?> attribute) {
            this.attribute = attribute;
        }
    }

    public enum RequestParams{
        FIRST_NAME("first_name"), LAST_NAME("last_name"), SORT("sort"), ORDER("order");

        public final String value;

        RequestParams(String value) {
            this.value = value;
        }
    }

    @Override
    public Map<String, String> getCriteriaAsMap(){
        Map<String, String> paramsMap = new LinkedHashMap<>();
        if (!firstNamePart.isBlank()){
            paramsMap.put(RequestParams.FIRST_NAME.value, firstNamePart);
        }
        if (!lastNamePart.isBlank()){
            paramsMap.put(RequestParams.LAST_NAME.value, lastNamePart);
        }
        paramsMap.put(RequestParams.ORDER.value, sortingOrder.toString().toLowerCase());
        paramsMap.put(RequestParams.SORT.value, sortingField.toString().toLowerCase());
        return paramsMap;
    }

}
