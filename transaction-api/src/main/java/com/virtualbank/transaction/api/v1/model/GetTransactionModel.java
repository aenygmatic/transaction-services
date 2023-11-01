package com.virtualbank.transaction.api.v1.model;

import java.time.Instant;
import java.util.UUID;

public record GetTransactionModel(UUID id,
                                  UUID userId,
                                  String description,
                                  String category,
                                  long amount,
                                  String currency,
                                  Instant timestamp,
                                  String status) {
}
