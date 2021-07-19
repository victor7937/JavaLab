package com.epam.esm.repository;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;


/**
 * Repository for manipulating users data in database
 */
public interface UserRepository {

    /**
     * Gets page with users from database
     * @param criteria - criteria with params for filtering and sorting
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with users found
     * @throws RepositoryException if page is not exist and troubles in database were happened
     */
    PagedDTO<User> getByCriteria(UserCriteria criteria, int pageSize, int pageNumber) throws RepositoryException;

    /**
     * Gets user by its email
     * @param email email of a user
     * @return user found
     * @throws RepositoryException if user with such email is not exists or troubles in database were happened
     */
    User getByEmail(String email) throws RepositoryException;

    User getById(Long id) throws RepositoryException;

    /**
     * Checks if user with such email exists in database
     * @param email - email of user for checking
     * @return true if user was found, else false
     */
    boolean isEmailExists(String email);

    boolean isIdExists(Long id);


    void persist(User user);
}
