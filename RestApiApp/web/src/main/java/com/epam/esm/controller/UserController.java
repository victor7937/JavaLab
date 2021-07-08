package com.epam.esm.controller;

import com.epam.esm.criteria.OrderCriteria;
import com.epam.esm.criteria.UserCriteria;
import com.epam.esm.dto.PagedDTO;
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
import com.epam.esm.util.StatusCodeGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.Map;


/**
 * Controller for REST operations with users and its orders
 * Makes get and get by id operations of users and their orders
 */
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

    /**
     * Get method for receiving a page of users by some criteria
     * @param size - size of page
     * @param page - number of current page
     * @param criteriaParams other params for filtering and sorting
     * @return Page of users
     */
    @GetMapping(produces = { "application/prs.hal-forms+json" })
    public PagedModel<UserModel> getUsers(@RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam Map<String, String> criteriaParams){
        PagedDTO<User> pagedDTO;
        UserCriteria criteria = UserCriteria.createCriteria(criteriaParams);
        try {
            pagedDTO = userService.get(criteria, size, page);
        } catch (IncorrectPageServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }

        if (pagedDTO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return userAssembler.toPagedModel(pagedDTO.getPage(), pagedDTO.getPageMetadata(), criteria);
    }

    /**
     * Get method for finding user by its email
     * @param email - email of a user
     * @return user found
     */
    @GetMapping(value = "/{email}", produces = { "application/prs.hal-forms+json" })
    public UserModel getByEmail(@PathVariable("email") String email){
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return userAssembler.toModel(user);
    }

    /**
     * Get method for receiving a page of orders by some criteria
     * @param email - email of a user
     * @param size - size of a page
     * @param page - current page
     * @param criteriaParams - Other params for filtering and sorting
     * @return page of orders
     */
    @GetMapping(value = "/{email}/orders", produces = { "application/prs.hal-forms+json" })
    public PagedModel<OrderModel> getOrdersOfUser(@PathVariable("email") String email,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam Map<String, String> criteriaParams){
        PagedDTO<Order> pagedDTO;
        OrderCriteria criteria = OrderCriteria.createCriteria(criteriaParams);
        try {
           pagedDTO = orderService.getOrdersOfUser(email, criteria, size, page);
        } catch (NotFoundServiceException | IncorrectPageServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        if (pagedDTO.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return orderAssembler.toPagedModel(pagedDTO.getPage(), pagedDTO.getPageMetadata(), criteria);
    }


    /**
     * Get method for receiving an order of a user by its id
     * @param email - users email
     * @param orderId - orders id
     * @return order found
     */
    @GetMapping(value = "/{email}/orders/{orderId}", produces = { "application/prs.hal-forms+json" })
    public OrderModel getOrderOfUser(@PathVariable("email") String email, @PathVariable("orderId") Long orderId){
        Order order;
        try {
           order = orderService.getOrder(email, orderId);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.NOT_FOUND, this.getClass()), e.getMessage(), e);
        } catch (IncorrectDataServiceException e) {
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.BAD_REQUEST, this.getClass()), e.getMessage(), e);
        } catch (ServiceException e) {
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(StatusCodeGenerator.getCode(HttpStatus.INTERNAL_SERVER_ERROR, this.getClass()), e.getMessage(), e);
        }
        return orderAssembler.toModel(order);
    }

    
}
