package com.epam.esm.criteria;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Order_;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

public class OrderCriteria {

    private static final String MIN_DATE_TIME = "2021-06-01T00:00:00";

    private SortingField sortingField;

    private SortingOrder sortingOrder;

    private Float minCost;

    private Float maxCost;

    private LocalDateTime minTime;

    private LocalDateTime maxTime;

    public OrderCriteria(Optional<SortingField> sortingField, Optional<SortingOrder> sortingOrder, Optional<Float> minCost,
                         Optional<Float> maxCost, Optional<LocalDateTime> minTime, Optional<LocalDateTime> maxTime){
        this.sortingField = sortingField.orElse(SortingField.ID);
        this.sortingOrder = sortingOrder.orElse(SortingOrder.ASC);
        this.minCost = minCost.orElse(0.0f);
        this.maxCost = maxCost.orElse(Float.MAX_VALUE);
        this.minTime = minTime.orElse(LocalDateTime.parse(MIN_DATE_TIME));
        this.maxTime = maxTime.orElse(LocalDateTime.now());

    }

    public static OrderCriteria createCriteria(Map<String, String> criteriaParams) {
        Optional<SortingField> sortingField = Optional.ofNullable(criteriaParams.get(RequestParams.SORT.value))
                .filter(s -> EnumUtils.isValidEnum(SortingField.class, s.toUpperCase()))
                .map(s -> SortingField.valueOf(s.toUpperCase()));


        Optional<SortingOrder> order = Optional.ofNullable(criteriaParams.get(RequestParams.ORDER.value))
                .filter(s -> EnumUtils.isValidEnum(SortingOrder.class, s.toUpperCase()))
                .map(s -> SortingOrder.valueOf(s.toUpperCase()));

        Optional<Float> minCost = Optional.ofNullable(criteriaParams.get(RequestParams.COST_GTE.value))
                .filter(NumberUtils::isCreatable)
                .map(Float::parseFloat);

        Optional<Float> maxCost = Optional.ofNullable(criteriaParams.get(RequestParams.COST_LTE.value))
                .filter(NumberUtils::isCreatable)
                .map(Float::parseFloat);

        Optional<LocalDateTime> minTime;
        try {
            minTime = Optional.ofNullable(criteriaParams.get(RequestParams.TIME_GTE.value)).map(LocalDateTime::parse);
        } catch (DateTimeParseException e){
            minTime = Optional.empty();
        }

        Optional<LocalDateTime> maxTime;
        try {
            maxTime = Optional.ofNullable(criteriaParams.get(RequestParams.TIME_LTE.value)).map(LocalDateTime::parse);
        } catch (DateTimeParseException e){
            maxTime = Optional.empty();
        }

        return new OrderCriteria(sortingField, order, minCost, maxCost, minTime, maxTime);
    }



    public SortingField getSortingField() {
        return sortingField;
    }

    public SortingOrder getSortingOrder() {
        return sortingOrder;
    }

    public Float getMinCost() {
        return minCost;
    }

    public Float getMaxCost() {
        return maxCost;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public enum SortingField {
        ID(Order_.id), COST(Order_.cost), TIME(Order_.timeOfPurchase);

        public final SingularAttribute<Order, ?> attribute;

        SortingField(SingularAttribute<Order, ?> attribute) {
            this.attribute = attribute;
        }
    }

    public enum RequestParams{
        COST_LTE("cost-lte"), COST_GTE("cost-gte"), TIME_LTE("time-lte"), TIME_GTE("time-gte"),
        SORT("sort"), ORDER("order");

        public final String value;

        RequestParams(String value) {
            this.value = value;
        }
    }

}

