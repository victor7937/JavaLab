package com.epam.esm.entity;

public class Criteria {

    private String tagName;


    public Criteria(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}