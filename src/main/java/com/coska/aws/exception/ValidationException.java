package com.coska.aws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Validation exception")
public class ValidationException extends RuntimeException {
    public enum Type{
        Duplicated("Duplicated:"),
        Invalid("Invalid:"),
        Empty("Empty:"),
        Error("Error:");
        public final String value;

        Type(String value) {
            this.value = value;
        }
    }
    public ValidationException() {
    }
    public ValidationException(final Type type, final String arg) {
        super(type.toString() + ": " + arg);
    }
    public ValidationException(final Type type, final List<String> args) {
        super(type.toString() + ": " + String.join(",", args));
    }
}
