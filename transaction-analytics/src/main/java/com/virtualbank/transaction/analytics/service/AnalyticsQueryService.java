package com.virtualbank.transaction.analytics.service;

import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;

import java.util.Optional;
import java.util.UUID;

public interface AnalyticsQueryService {

    Optional<MonthlyAnalytics> getForNow(UUID userId);

    Optional<MonthlyAnalytics> getFor(UUID userId, int year, int month);
}
