package com.epam.esm.repository;

import com.epam.esm.entity.User;
import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface UserRepository {
    List<User> getAll();
    User getByEmail(String email) throws RepositoryException;
}
