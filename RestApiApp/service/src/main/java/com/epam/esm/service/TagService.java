package com.epam.esm.service;

import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;

import java.util.List;

/**
 * Service for manipulating tags data
 */
public interface TagService {

    /**
     * Gets page with tags from data source
     * @param namePart - part of tags name for searching
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with tags found
     * @throws ServiceException if pagination params are incorrect or some troubles in data source were happened
     */
    PagedDTO<Tag> get(String namePart, int pageSize, int pageNumber) throws ServiceException;

    /**
     * Get one tag if id is correct
     * @param id - id of the tag
     * @return tag found
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    Tag getById(Long id) throws ServiceException;

    /**
     * Add a new tag if such tag is not exist in data source
     * @param tag - tag for adding
     * @throws ServiceException when params is incorrect, such tag exists in data source or some
     * troubles in data source were happened
     */
    void add(Tag tag) throws ServiceException;

    /**
     * Delete tag
     * @param id - id of the tag for deleting
     * @throws ServiceException if id is incorrect or some troubles in data source were happened
     */
    void delete(Long id) throws ServiceException;


    /**
     * Gets the most widely used tag of a user with the highest cost of all orders
     * @return Tag found
     */
    Tag getMostUsedTagOfValuableCustomer();
}
