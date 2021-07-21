package com.epam.esm.service.impl;

import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.*;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.CriteriaValidator;
import com.epam.esm.validator.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    private static final String PASSWORD_IS_INVALID_MSG = "password is invalid";
    private final UserRepository userRepository;
    private final CriteriaValidator<UserCriteria> criteriaValidator;
    private final ServiceValidator<User> serviceValidator;

    private static final String NOT_EXIST_EMAIL_MSG = "User with email %s doesn't exist";
    private static final String NOT_EXIST_MSG = "User with id %s doesn't exist";
    private static final String NO_SUCH_PAGE_MSG = "Page with number %s doesn't exist";
    private static final String INCORRECT_PARAMS_MSG = "Incorrect request parameter values";
    private static final String INCORRECT_USERS_EMAIL_MSG = "Users email is incorrect";
    private static final String INCORRECT_USER_MSG = "Users id is incorrect";


    @Autowired
    public UserServiceImpl(UserRepository userRepository, CriteriaValidator<UserCriteria> criteriaValidator, ServiceValidator<User> serviceValidator) {
        this.userRepository = userRepository;
        this.criteriaValidator = criteriaValidator;
        this.serviceValidator = serviceValidator;
    }

    @Override
    public PagedDTO<User> get(UserCriteria criteria, int pageSize, int pageNumber) throws ServiceException{
        PagedDTO<User> pagedDTO;
        if (!(criteriaValidator.validateCriteria(criteria) && serviceValidator.isPageParamsValid(pageSize, pageNumber))){
            throw new IncorrectDataServiceException(INCORRECT_PARAMS_MSG);
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
        if (!serviceValidator.isStringIdValid(email)){
            throw new IncorrectDataServiceException(INCORRECT_USERS_EMAIL_MSG);
        }

        User user;
        try {
            user = userRepository.getByEmail(email);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_EMAIL_MSG, email), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public User getById(Long id) throws ServiceException {
        if (!serviceValidator.isLongIdValid(id)){
            throw new IncorrectDataServiceException(INCORRECT_USER_MSG);
        }

        User user;
        try {
            user = userRepository.getById(id);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(NOT_EXIST_MSG, id), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public void registration(UserDTO userDTO) throws ServiceException {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        if (!(serviceValidator.isStringIdValid(email) && password != null && !password.isBlank())){
            throw new IncorrectDataServiceException(PASSWORD_IS_INVALID_MSG);
        }
        if (userRepository.isEmailExists(email)){
            throw new AlreadyExistServiceException(String.format("Email %s already exists", email));
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        User user = new User(email, passwordEncoder.encode(password), userDTO.getFirstName(), userDTO.getLastName());
        userRepository.add(user);
    }
}
