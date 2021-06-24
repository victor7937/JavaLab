package com.epam.esm.message;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;


/**
 * Message that contains exception or error data
 */
public class ResponseExceptionMessage implements Serializable {

    private static final long serialVersionUID = -1513236421502919101L;

    private int statusCode;

    private HttpStatus status;

    private String message;

    public ResponseExceptionMessage(){}

    public ResponseExceptionMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.statusCode = status.value();
    }

    public ResponseExceptionMessage(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = HttpStatus.valueOf(statusCode / 10);
    }



    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseExceptionMessage that = (ResponseExceptionMessage) o;
        return status == that.status && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public String toString() {
        return "ResponseExceptionMessage{" +
                "statusCode=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
