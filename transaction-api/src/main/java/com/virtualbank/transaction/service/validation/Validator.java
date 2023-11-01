package com.virtualbank.transaction.service.validation;

import com.virtualbank.transaction.service.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface Validator {

    //TODO: Use custom error object instead of String
    List<String> validate(Transaction transaction);
}
