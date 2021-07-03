package com.epam.esm.entity;

import com.epam.esm.util.CustomLocalDateTimeDeserializer;
import com.epam.esm.util.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = -2459886736436355958L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @CreationTimestamp
    @Column(name = "time_of_purchase")
    private LocalDateTime timeOfPurchase;

    @Column(name = "cost")
    private Float cost;

    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "certificate_id")
    private GiftCertificate giftCertificate;

    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REMOVE,
            CascadeType.REFRESH})
    @JoinColumn(name = "users_email")
    private User user;

    public Order() {}

    public Order(Long id, LocalDateTime timeOfPurchase, Float cost) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(timeOfPurchase, order.timeOfPurchase) && Objects.equals(cost, order.cost) && Objects.equals(giftCertificate, order.giftCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeOfPurchase, cost, giftCertificate);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", timeOfPurchase=" + timeOfPurchase +
                ", cost=" + cost +
                ", giftCertificate=" + giftCertificate +
                '}';
    }
}
