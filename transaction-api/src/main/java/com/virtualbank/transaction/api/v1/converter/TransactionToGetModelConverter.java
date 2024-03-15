package com.virtualbank.transaction.api.v1.converter;

import com.virtualbank.transaction.api.v1.model.GetTransactionModel;
import com.virtualbank.transaction.service.model.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionToGetModelConverter implements Converter<Transaction, GetTransactionModel> {

    @Override
    public GetTransactionModel convert(Transaction source) {
        return new GetTransactionModel(source.getId(),
                source.getUserId(),
                source.getDescription(),
                source.getCategory(),
                source.getAmount(),
                source.getCurrency(),
                source.getTimestamp(),
                source.getStatus().toString());
    }
}
