package com.epam.esm.repository;

import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RepositoryException;


/**
 * Repository for manipulating tag data in database
 */
public interface TagRepository {

    /**
     * Gets page with tags form database by name part
     * @param namePart - part of tags name for searching
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with tags which match the name part
     * @throws RepositoryException if no such page or some troubles in database were happened
     */
    PagedDTO<Tag> get(String namePart, int pageSize, int pageNumber) throws RepositoryException;

    /**
     * Get one tag if id is correct from database
     * @param id - id of the tag
     * @return tag found
     * @throws RepositoryException if such id exists or some troubles in database were happened
     */
    Tag getById(Long id) throws RepositoryException;

    Tag getByName(String name);

    boolean isTagExists(String name);

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
    void delete(Long id) throws RepositoryException;

    /**
     * Gets the most widely used tag of a user with the highest cost of all orders from database
     * @return tag found
     */
    Tag getMostUsedTagOfValuableCustomer ();

}
