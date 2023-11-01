package com.virtualbank.transaction.service.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(UUID id) {
        super("Transaction not found! transactionId=" + id);
    }
}
