package com.epam.esm.service;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ServiceException;


import java.util.List;

public interface UserService {
    PagedDTO<User> get(UserCriteria criteria, int pageSize, int pageNumber) throws ServiceException;
    User getByEmail(String email) throws ServiceException;
}
