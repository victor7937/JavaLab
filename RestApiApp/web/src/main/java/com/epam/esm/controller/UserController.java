package com.epam.esm.controller;

import com.epam.esm.criteria.CertificateCriteria;
import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.IncorrectDataServiceException;
import com.epam.esm.exception.IncorrectPageServiceException;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.assembler.UserAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;


import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final UserAssembler userAssembler;
    private final OrderAssembler orderAssembler;

    Logger logger = Logger.getLogger(UserController.class);

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in User Controller";

    @Autowired
    public UserController(UserService userService, OrderService orderService, UserAssembler userAssembler, OrderAssembler orderAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userAssembler = userAssembler;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping(produces = { "application/prs.hal-forms+json" })
    public PagedModel<UserModel> getAllUsers(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                             @RequestParam Map<String, String> criteriaParams){
        PagedDTO<User> pagedDTO;
        try {
            pagedDTO = userService.get(UserCriteria.createCriteria(criteriaParams), size, page);
        } catch (IncorrectPageServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.BAD_REQUEST), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }

        return userAssembler.toPagedModel(pagedDTO.getPage(), pagedDTO.getPageMetadata());
    }

    @GetMapping(value = "/{email}", produces = { "application/prs.hal-forms+json" })
    public UserModel getByEmail(@PathVariable("email") String email){
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return userAssembler.toModel(user);
    }

    @GetMapping(value = "/{email}/orders", produces = { "application/prs.hal-forms+json" })
    public CollectionModel<OrderModel> getOrdersOfUser(@PathVariable("email") String email){
        List<Order> orders;

        try {
            orders = orderService.getOrders(email);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }

        return orderAssembler.toCollectionModel(orders);
    }

    @GetMapping(value = "/{email}/orders/{orderId}", produces = { "application/prs.hal-forms+json" })
    public OrderModel getOrderOfUser(@PathVariable("email") String email, @PathVariable("orderId") Long orderId){
        Order order;
        try {
           order = orderService.getOrder(email, orderId);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return orderAssembler.toModel(order);
    }

    private int generateStatusCode(HttpStatus status){
        return status.value() * 10 + 3;
    }
}
