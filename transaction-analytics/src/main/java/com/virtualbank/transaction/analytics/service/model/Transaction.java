package com.virtualbank.transaction.analytics.service.model;

import java.time.Instant;
import java.util.UUID;

public record Transaction(UUID id,
                          String category,
                          MonetaryAmount amount,
                          Instant timestamp) {
}
