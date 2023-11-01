package com.virtualbank.transaction.service.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    //TODO: Use custom error object instead of String
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super("Invalid transaction! errors=" + String.join(",", errors));
        this.errors = errors;
    }
}
