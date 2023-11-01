package com.virtualbank.transaction.analytics.service;

import com.virtualbank.transaction.analytics.service.model.Projections;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ProjectionCalculator {

    Projections calculate(UUID userId, ZonedDateTime time);
}
