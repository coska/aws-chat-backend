package com.coska.aws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Not found")
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }
    public NotFoundException(String msg) {
        super(msg);
    }
}