package com.virtualbank.transaction.service.validation;

import com.virtualbank.transaction.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CurrencyValidator implements Validator {

    @Value("${validation.currencies}")
    private final Set<String> currencies;

    @Override
    public List<String> validate(Transaction transaction) {
        if (!currencies.contains(transaction.getCurrency())) {
            return List.of(String.format("Invalid currency '%s'! Valid currencies: %s",
                    transaction.getCurrency(),
                    String.join(",", currencies)));
        } else {
            return Collections.emptyList();
        }
    }
}
