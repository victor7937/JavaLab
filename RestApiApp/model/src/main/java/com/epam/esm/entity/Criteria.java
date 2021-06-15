package com.epam.esm.entity;

/**
 * Criteria for searching by tag
 */
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
