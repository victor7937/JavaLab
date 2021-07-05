package com.epam.esm.service.impl;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.*;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final String NOT_EXIST_MSG = "User with email %s doesn't exist";
    private static final String INVALID_PAGE_PARAMS = "Page params are invalid";
    private static final String NO_SUCH_PAGE_MSG = "Page with number %s doesn't exist";

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public PagedDTO<User> get(UserCriteria criteria, int pageSize, int pageNumber) throws ServiceException{
        PagedDTO<User> pagedDTO;
        if (pageSize < 1 || pageNumber < 1){
            throw new IncorrectDataServiceException(INVALID_PAGE_PARAMS);
        }
        try {
            pagedDTO = userRepository.getByCriteria(criteria, pageSize, pageNumber);
        } catch (IncorrectPageRepositoryException e) {
            throw new IncorrectPageServiceException(String.format(NO_SUCH_PAGE_MSG, pageNumber), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return pagedDTO;
    }

    @Override
    public User getByEmail(String email) throws ServiceException {

        User user;
        try {
            user = userRepository.getByEmail(email);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, email), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return user;
    }
}
