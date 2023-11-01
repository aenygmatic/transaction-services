package com.virtualbank.transaction.service.validation;

import com.virtualbank.transaction.service.model.Transaction;
import com.virtualbank.transaction.service.model.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private final List<Validator> validators;

    public void validate(Transaction transaction) {
        List<String> errors = validators.stream()
                .flatMap(validator -> validator.validate(transaction).stream())
                .collect(Collectors.toList());

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
