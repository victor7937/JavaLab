package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

/**
 * Repository for manipulating tag data in certificates-tag database
 */
public interface TagRepository {

    /**
     * Get all tags from database
     * @return list of all tags
     */
    List<Tag> getAll();

    /**
     * Get one tag if id is correct from database
     * @param id - id of the tag
     * @return tag found
     * @throws RepositoryException if such id exists or some troubles in database were happened
     */
    Tag getById(int id) throws RepositoryException;


    /**
     * Add a new tag if such tag is not exist in database
     * @param tag - tag for adding
     * @throws RepositoryException when such tag exists in database or some troubles in database
     * were happened
     */
    void add(Tag tag) throws RepositoryException;

    /**
     * Delete tag from database
     * @param id - id of gift certificate for deleting
     * @throws RepositoryException if such id exists or some troubles in database were happened
     */
    void delete(int id) throws RepositoryException;
}
