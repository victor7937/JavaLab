package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;


import java.util.List;

public interface UserService {
    List<User> getAll();
    User getByEmail(String email) throws ServiceException;
}
