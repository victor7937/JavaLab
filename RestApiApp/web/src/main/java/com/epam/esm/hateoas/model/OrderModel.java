package com.epam.esm.hateoas.model;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.CustomLocalDateTimeDeserializer;
import com.epam.esm.util.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


import java.time.LocalDateTime;
import java.util.Objects;

@Relation(itemRelation = "order", collectionRelation = "orders")
public class OrderModel extends RepresentationModel<OrderModel> {

    private Long id;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime timeOfPurchase;

    private Float cost;

    private GiftCertificate giftCertificate;

    private UserModel user;

    public OrderModel() {
    }

    public OrderModel(Long id, LocalDateTime timeOfPurchase, Float cost) {
        this.id = id;
        this.timeOfPurchase = timeOfPurchase;
        this.cost = cost;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeOfPurchase() {
        return timeOfPurchase;
    }

    public void setTimeOfPurchase(LocalDateTime timeOfPurchase) {
        this.timeOfPurchase = timeOfPurchase;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderModel that = (OrderModel) o;
        return Objects.equals(id, that.id) && Objects.equals(timeOfPurchase, that.timeOfPurchase)
                && Objects.equals(cost, that.cost) && Objects.equals(giftCertificate, that.giftCertificate)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, timeOfPurchase, cost, giftCertificate, user);
    }
}
