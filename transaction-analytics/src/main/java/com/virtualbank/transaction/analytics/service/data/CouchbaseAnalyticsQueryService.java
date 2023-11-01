package com.virtualbank.transaction.analytics.service.data;

import com.virtualbank.transaction.analytics.service.AnalyticsQueryService;
import com.virtualbank.transaction.analytics.service.data.couchbase.MonthlyAnalyticsRepository;
import com.virtualbank.transaction.analytics.service.model.IdGenerator;
import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouchbaseAnalyticsQueryService implements AnalyticsQueryService {

    private final MonthlyAnalyticsRepository repository;

    @Override
    public Optional<MonthlyAnalytics> getForNow(UUID userId) {
        return repository.findById(IdGenerator.idForNow(userId));
    }

    @Override
    public Optional<MonthlyAnalytics> getFor(UUID userId, int year, int month) {
        return repository.findById(IdGenerator.idFor(userId, year, month));
    }
}
