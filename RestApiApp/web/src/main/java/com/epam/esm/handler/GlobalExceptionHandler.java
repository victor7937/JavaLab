package com.epam.esm.handler;

import com.epam.esm.message.ResponseExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(ResponseStatusException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(e.getStatus(), e.getReason()), e.getStatus());
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(NumberFormatException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    //Было бы интересно давать разные статус коды для разных исключений, а не просто 500
    //Пока не знаю как так сделать(может и не надо)
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ResponseExceptionMessage> handleException(RuntimeException e){
        return new ResponseEntity<>(new ResponseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}