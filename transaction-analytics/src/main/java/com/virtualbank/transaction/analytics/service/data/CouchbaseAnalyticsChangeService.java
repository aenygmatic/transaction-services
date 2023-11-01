package com.virtualbank.transaction.analytics.service.data;

import com.virtualbank.transaction.analytics.service.AnalyticsChangeService;
import com.virtualbank.transaction.analytics.service.ProjectionCalculator;
import com.virtualbank.transaction.analytics.service.StatisticsService;
import com.virtualbank.transaction.analytics.service.data.couchbase.MonthlyAnalyticsRepository;
import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;
import com.virtualbank.transaction.analytics.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.virtualbank.transaction.analytics.service.model.IdGenerator.idFor;

@Service
@RequiredArgsConstructor
public class CouchbaseAnalyticsChangeService implements AnalyticsChangeService {

    private final MonthlyAnalyticsRepository repository;
    private final StatisticsService statisticsService;
    private final ProjectionCalculator projectionCalculator;

    @Override
    public void addTransaction(UUID userId, Transaction transaction) {
        createOrUpdateTransaction(userId,
                transaction.id(),
                transaction.timestamp(),
                transactions -> transactions.add(transaction));
    }

    @Override
    public void updateTransaction(UUID userId, Transaction transaction) {
        createOrUpdateTransaction(userId,
                transaction.id(),
                transaction.timestamp(),
                transactions -> transactions.add(transaction));
    }

    @Override
    public void deleteTransaction(UUID userId, Transaction transaction) {
        createOrUpdateTransaction(userId,
                transaction.id(),
                transaction.timestamp(),
                noAction());
    }

    private void createOrUpdateTransaction(UUID userId,
                                           UUID transactionId,
                                           Instant transactionTime,
                                           Consumer<List<Transaction>> replaceAction) {
        ZonedDateTime time = transactionTime.atZone(ZoneId.systemDefault());

        MonthlyAnalytics analytics = repository.findById(idFor(userId, time))
                .map(current -> updatedMonthlyAnalytics(transactionId, current, replaceAction))
                .orElseGet(() -> newMonthlyAnalytics(userId, time, replaceAction));

        repository.save(analytics);
    }

    private MonthlyAnalytics newMonthlyAnalytics(UUID userId,
                                                 ZonedDateTime time,
                                                 Consumer<List<Transaction>> replaceAction) {
        List<Transaction> transactions = new ArrayList<>();
        replaceAction.accept(transactions);
        return MonthlyAnalytics.builder()
                .id(idFor(userId, time))
                .transactions(transactions)
                .projections(projectionCalculator.calculate(userId, time))
                .statistics(statisticsService.calculate(transactions))
                .build();
    }

    private MonthlyAnalytics updatedMonthlyAnalytics(UUID transactionId,
                                                     MonthlyAnalytics analytics,
                                                     Consumer<List<Transaction>> replaceAction) {
        analytics.setTransactions(updateTransactions(
                transactionId,
                analytics.getTransactions(),
                replaceAction));

        analytics.setStatistics(statisticsService.calculate(analytics.getTransactions()));
        return analytics;
    }

    private static List<Transaction> updateTransactions(UUID transactionId,
                                                        List<Transaction> oldTransactions,
                                                        Consumer<List<Transaction>> replaceAction) {
        List<Transaction> cleanedTransaction = oldTransactions.stream()
                .filter(transaction -> !transaction.id().equals(transactionId))
                .collect(Collectors.toList());

        replaceAction.accept(cleanedTransaction);

        return cleanedTransaction;
    }

    private static Consumer<List<Transaction>> noAction() {
        return transactions -> {
        };
    }

}
