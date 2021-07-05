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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final GiftCertificateRepository certificateRepository;

    private static final String USER_NOT_EXIST_MSG = "User with email  %s doesn't exist";
    private static final String CERTIFICATE_NOT_EXIST_MSG = "Certificate with id %s doesn't exist";
    private static final String INCORRECT_ORDER_MSG = "Incorrect order id or users email";
    private static final String NO_SUCH_PAGE_MSG = "Page with number %s doesn't exist";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, GiftCertificateRepository certificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    public Order makeOrder(OrderDTO orderDTO) throws ServiceException {
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
    public PagedDTO<Order> get(String userEmail, OrderCriteria criteria, int pageSize, int pageNumber) throws ServiceException {
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
