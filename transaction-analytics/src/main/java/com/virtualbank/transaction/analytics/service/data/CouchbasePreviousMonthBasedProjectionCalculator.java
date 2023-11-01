package com.virtualbank.transaction.analytics.service.data;

import com.virtualbank.transaction.analytics.service.ProjectionCalculator;
import com.virtualbank.transaction.analytics.service.data.couchbase.MonthlyAnalyticsRepository;
import com.virtualbank.transaction.analytics.service.model.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.virtualbank.transaction.analytics.service.model.IdGenerator.idFor;

@Component
@RequiredArgsConstructor
public class CouchbasePreviousMonthBasedProjectionCalculator implements ProjectionCalculator {

    private final MonthlyAnalyticsRepository repository;

    public Projections calculate(UUID userId, ZonedDateTime time) {
        return repository.findById(idFor(userId, time.minusMonths(1)))
                .map(analytics -> new Projections(
                        analytics.getStatistics().totalSpending(),
                        analytics.getStatistics().totalIncome()))
                .orElse(Projections.empty());
    }
}
