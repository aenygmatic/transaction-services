package com.virtualbank.transaction.analytics.api.event.model;

import java.time.Instant;
import java.util.UUID;

public record TransactionEvent(Type type, Data content) {

    public enum Type {
        CREATED, UPDATED, DELETED;
    }

    public record Data(UUID id,
                       UUID userId,
                       String description,
                       String category,
                       long amount,
                       String currency,
                       Instant timestamp,
                       String status) {
    }

}
