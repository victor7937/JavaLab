package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;

import java.util.List;

/**
 * Service for manipulating tags data
 */
public interface TagService {

    /**
     * Get all tags
     * @return list of tags
     */
    List<Tag> getAll();

    /**
     * Get one tag if id is correct
     * @param id - id of the tag
     * @return founded tag
     * @throws ServiceException if id is incorrect or some troubles in data source was happened
     */
    Tag getById(Integer id) throws ServiceException;

    /**
     * Add a new tag if such tag is not exist in data source
     * @param tag - tag for adding
     * @throws ServiceException when params is incorrect, such tag exists in data source or some
     * troubles in data source was happened
     */
    void add(Tag tag) throws ServiceException;

    /**
     * Delete tag
     * @param id - id of the tag for deleting
     * @throws ServiceException if id is incorrect or some troubles in data source was happened
     */
    void delete(Integer id) throws ServiceException;
}
