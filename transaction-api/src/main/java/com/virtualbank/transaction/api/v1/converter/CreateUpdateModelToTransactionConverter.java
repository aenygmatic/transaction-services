package com.virtualbank.transaction.api.v1.converter;

import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import com.virtualbank.transaction.service.model.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUpdateModelToTransactionConverter implements Converter<CreateUpdateTransactionModel, Transaction> {

    @Override
    public Transaction convert(CreateUpdateTransactionModel model) {
        return Transaction.builder()
                .userId(model.userId())
                .category(model.category())
                .description(model.description())
                .amount(model.amount())
                .currency(model.currency())
                .timestamp(model.timestamp())
                .build();
    }
}
