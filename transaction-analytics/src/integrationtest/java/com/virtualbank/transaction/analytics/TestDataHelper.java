package com.virtualbank.transaction.analytics;

import com.virtualbank.transaction.analytics.api.event.model.TransactionEvent;

import java.time.Instant;
import java.util.UUID;

public interface TestDataHelper {

    default TransactionEvent newCreateTransactionEvent(UUID userId,
                                                       String category,
                                                       int amount,
                                                       String currency,
                                                       Instant timestamp) {
        return new TransactionEvent(TransactionEvent.Type.CREATED,
                new TransactionEvent.Data(
                        UUID.randomUUID(),
                        userId,
                        "dec",
                        category,
                        amount,
                        currency,
                        timestamp,
                        "CREATED"
                ));
    }

}
