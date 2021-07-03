package com.epam.esm.hateoas.model;

import com.epam.esm.entity.Order;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(itemRelation = "user", collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {

    private String email;

    private String name;

    private String surname;

    public UserModel() {
    }

    public UserModel(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(email, userModel.email) && Objects.equals(name, userModel.name) && Objects.equals(surname, userModel.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, name, surname);
    }
}
