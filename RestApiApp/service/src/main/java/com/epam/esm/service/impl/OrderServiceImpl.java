package com.epam.esm.service.impl;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.*;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.CriteriaValidator;
import com.epam.esm.validator.ServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OrderServiceImpl implements OrderService {


    private static final String EMAIL_NOT_VALID = "User email is not valid";
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final GiftCertificateRepository certificateRepository;

    private final CriteriaValidator<OrderCriteria> criteriaValidator;

    private final ServiceValidator<OrderDTO> validator;

    private static final String USER_NOT_EXIST_MSG = "User with email %s doesn't exist";
    private static final String CERTIFICATE_NOT_EXIST_MSG = "Certificate with id %s doesn't exist";
    private static final String INCORRECT_ORDER_MSG = "Incorrect order id or users email";
    private static final String NO_SUCH_PAGE_MSG = "Page with number %s doesn't exist";
    private static final String INCORRECT_PARAMS_MSG = "Incorrect request parameter values";
    private static final String INCORRECT_ORDER_PARAMS_MSG = "Incorrect order details";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, GiftCertificateRepository certificateRepository,
                            CriteriaValidator<OrderCriteria> criteriaValidator, ServiceValidator<OrderDTO> validator) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.criteriaValidator = criteriaValidator;
        this.validator = validator;
    }

    @Override
    public Order makeOrder(OrderDTO orderDTO) throws ServiceException {
        if (!validator.validate(orderDTO)){
            throw new IncorrectDataServiceException(INCORRECT_ORDER_PARAMS_MSG);
        }

        User user;
        try {
            user = userRepository.getByEmail(orderDTO.getUsersEmail());
        } catch (DataNotExistRepositoryException e){
            throw new NotFoundServiceException(String.format(USER_NOT_EXIST_MSG, orderDTO.getUsersEmail()), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        GiftCertificate certificate;
        try {
            certificate = certificateRepository.getById(orderDTO.getCertificateId());
        } catch (DataNotExistRepositoryException e){
            throw new NotFoundServiceException(String.format(CERTIFICATE_NOT_EXIST_MSG, orderDTO.getCertificateId()), e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }

        Order order = new Order();
        order.setGiftCertificate(certificate);
        order.setCost(certificate.getPrice());
        user.addOrder(order);

        return orderRepository.makeOrder(order);
    }

    @Override
    public PagedDTO<Order> getOrdersOfUser(String userEmail, OrderCriteria criteria, int pageSize, int pageNumber) throws ServiceException {
        if (!(criteriaValidator.validateCriteria(criteria) && validator.isPageParamsValid(pageSize, pageNumber))){
            throw new IncorrectDataServiceException(INCORRECT_PARAMS_MSG);
        }
        if (!validator.isStringIdValid(userEmail)){
            throw new IncorrectDataServiceException(EMAIL_NOT_VALID);
        }
        PagedDTO<Order> orders;
        try {
            orders = orderRepository.getOrders(userEmail, criteria, pageSize, pageNumber);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(String.format(USER_NOT_EXIST_MSG, userEmail), e);
        } catch (IncorrectPageRepositoryException e) {
            throw new IncorrectPageServiceException(String.format(NO_SUCH_PAGE_MSG, pageNumber), e);
        } catch (RepositoryException e){
            throw new ServiceException(e);
        }
        return orders;
    }

    @Override
    public Order getOrder(String userEmail, Long orderId) throws ServiceException {
        Order order;
        if (!(validator.isLongIdValid(orderId) && validator.isStringIdValid(userEmail))){
            throw new IncorrectDataServiceException(INCORRECT_ORDER_MSG);
        }

        try {
            order = orderRepository.getOrder(userEmail, orderId);
        } catch (DataNotExistRepositoryException e) {
            throw new NotFoundServiceException(INCORRECT_ORDER_MSG, e);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
        return order;
    }
}
