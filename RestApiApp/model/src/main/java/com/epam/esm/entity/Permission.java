package com.epam.esm.entity;

public enum Permission {

    CERTIFICATES_READ("certificates:read"), CERTIFICATES_WRITE("certificates:write"),
    CERTIFICATES_BUY("certificates:buy"), TAGS_READ("tags:read"), TAGS_WRITE("tags:write"),
    USERS_READ("users:read"), ORDERS_READ("orders:read");

    public final String name;

    Permission(String name) {
        this.name = name;
    }

}
