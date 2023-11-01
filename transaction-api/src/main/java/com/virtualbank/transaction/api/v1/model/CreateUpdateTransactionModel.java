package com.virtualbank.transaction.api.v1.model;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CreateUpdateTransactionModel(UUID userId,
                                           String description,
                                           String category,
                                           long amount,
                                           String currency,
                                           Instant timestamp) {
}
