package com.epam.esm.controller;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.NotFoundServiceException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    Logger logger = Logger.getLogger(UserController.class);

    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in User Controller";

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable("email") String email){
        User user;
        try {
            user = userService.getByEmail(email);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e){
            logger.error(EXCEPTION_CAUGHT_MSG, e);
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return user;
    }

    @GetMapping("/{email}/orders")
    public List<Order> getOrdersOfUser(@PathVariable("email") String email){
        List<Order> orders;
        try {
            orders = orderService.getOrders(email);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return orders;
    }

    @GetMapping("/{email}/orders/{orderId}")
    public Order getOrderOfUser(@PathVariable("email") String email, @PathVariable("orderId") Long orderId){
        Order order;
        try {
           order = orderService.getOrder(email, orderId);
        } catch (NotFoundServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.NOT_FOUND), e.getMessage(), e);
        } catch (ServiceException e) {
            throw new ResponseStatusException(generateStatusCode(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), e);
        }
        return order;
    }

    private int generateStatusCode(HttpStatus status){
        return status.value() * 10 + 3;
    }
}
