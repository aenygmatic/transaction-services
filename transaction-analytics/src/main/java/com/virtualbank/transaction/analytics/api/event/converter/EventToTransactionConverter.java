package com.virtualbank.transaction.analytics.api.event.converter;


import com.virtualbank.transaction.analytics.api.event.model.TransactionEvent;
import com.virtualbank.transaction.analytics.service.model.MonetaryAmount;
import com.virtualbank.transaction.analytics.service.model.Transaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventToTransactionConverter implements Converter<TransactionEvent.Data, Transaction> {

    @Override
    public Transaction convert(TransactionEvent.Data source) {
        return new Transaction(
                source.id(),
                source.category(),
                new MonetaryAmount(source.amount(), source.currency()),
                source.timestamp());
    }
}
