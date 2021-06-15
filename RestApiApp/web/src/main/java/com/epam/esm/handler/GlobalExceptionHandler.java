package com.epam.esm.handler;

import com.epam.esm.message.ResponseExceptionMessage;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;


/**
 * Global controller for handling all exceptions were caught
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    private static final String ARGUMENT_INVALID_MSG = "Argument in path is invalid";
    private static final String EXCEPTION_CAUGHT_MSG = "Exception was caught in GlobalExceptionHandler:";
    private static final String DATABASE_ERROR_MSG = "Database Error";
    private static final String SERVER_ERROR_MSG = "Server Error";

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(ResponseStatusException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(e.getStatus(), e.getReason()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(MethodArgumentTypeMismatchException e){
        logger.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.BAD_REQUEST, ARGUMENT_INVALID_MSG), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(DataAccessException e){
        logger.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, DATABASE_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(Exception e){
        logger.error(EXCEPTION_CAUGHT_MSG, e);
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, SERVER_ERROR_MSG), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}