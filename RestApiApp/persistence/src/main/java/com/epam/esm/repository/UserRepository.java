package com.epam.esm.repository;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface UserRepository {
    PagedDTO<User> getByCriteria(UserCriteria criteria, int pageSize, int pageNumber) throws RepositoryException;
    User getByEmail(String email) throws RepositoryException;
    boolean isUserExists(String email);
}
