package com.virtualbank.transaction.analytics.service;

import com.virtualbank.transaction.analytics.service.model.MonetaryAmount;
import com.virtualbank.transaction.analytics.service.model.Statistics;
import com.virtualbank.transaction.analytics.service.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsServiceTest {

    private final UUID ignoredId = UUID.randomUUID();
    private final Instant ignoredTime = Instant.now();
    private final StatisticsService statisticsService = new StatisticsService();

    @Test
    public void testCalculate() {
        List<Transaction> transactions = List.of(
                transaction("salary", 9000, "EUR"),
                transaction("salary", 2000, "GBP"),
                transaction("investment", 2000, "USD"),
                transaction("restaurant", -200, "EUR"),
                transaction("restaurant", -300, "EUR"),
                transaction("restaurant", -200, "GBP"),
                transaction("restaurant", -20000, "HUF"),
                transaction("groceries", -8000, "HUF"),
                transaction("groceries", -6000, "HUF"),
                transaction("groceries", -6000, "HUF"),
                transaction("groceries", -6000, "HUF")
        );

        Statistics statistics = statisticsService.calculate(transactions);

        assertEquals(List.of(
                        new MonetaryAmount(9000, "EUR"),
                        new MonetaryAmount(2000, "GBP"),
                        new MonetaryAmount(2000, "USD")),
                statistics.totalIncome());
        assertEquals(List.of(
                        new MonetaryAmount(-500, "EUR"),
                        new MonetaryAmount(-200, "GBP"),
                        new MonetaryAmount(-46_000, "HUF")),
                statistics.totalSpending());
        assertEquals(Map.of(
                        "salary", List.of(
                                new MonetaryAmount(9000, "EUR"),
                                new MonetaryAmount(2000, "GBP")),
                        "investment", List.of(
                                new MonetaryAmount(2000, "USD"))),
                statistics.incomeByCategory());
        assertEquals(Map.of(
                        "restaurant", List.of(
                                new MonetaryAmount(-500, "EUR"),
                                new MonetaryAmount(-200, "GBP"),
                                new MonetaryAmount(-20_000, "HUF")),
                        "groceries", List.of(
                                new MonetaryAmount(-26_000, "HUF"))),
                statistics.spendingByCategory());
    }

    private Transaction transaction(String category, long amount, String currency) {
        return new Transaction(ignoredId, category, new MonetaryAmount(amount, currency), ignoredTime);
    }
}