package com.epam.esm.service;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;


/**
 * Service for manipulating users data
 */
public interface UserService {

    /**
     * Gets page with users from data source
     * @param criteria - criteria with params for filtering and sorting
     * @param pageSize - size of one page
     * @param pageNumber - number of a current page
     * @return page with users found
     * @throws ServiceException if criteria or pagination params are incorrect
     */
    PagedDTO<User> get(UserCriteria criteria, int pageSize, int pageNumber);

    /**
     * Gets user by its email
     * @param email email of a user
     * @return user found
     * @throws ServiceException if email is incorrect
     */
    User getByEmail(String email);

    User getById(Long id);

    void registration(UserDTO userDTO);
}
