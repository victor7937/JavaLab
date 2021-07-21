package com.epam.esm.handler;

import com.epam.esm.message.ResponseExceptionMessage;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;


/**
 * Global controller for handling all exceptions were caught
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String ARGUMENT_INVALID_MSG = "Argument in path is invalid";
    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in GlobalExceptionHandler:";
    private static final String DATABASE_ERROR_MSG = "Database Error";
    private static final String SERVER_ERROR_MSG = "Server Error";
    private static final String USER_NOT_FOUND = "User is not found";

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(ResponseStatusException e){
        ResponseExceptionMessage message = new ResponseExceptionMessage(e.getRawStatusCode(), e.getReason());
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(UsernameNotFoundException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.NOT_FOUND, USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(BadCredentialsException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.FORBIDDEN, "Wrong email or password"), HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(AccessDeniedException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.FORBIDDEN, "Access denied for this role"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(MethodArgumentTypeMismatchException e){
        log.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.BAD_REQUEST, ARGUMENT_INVALID_MSG), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(DataAccessException e){
        log.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, DATABASE_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(Exception e){
        log.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, SERVER_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}