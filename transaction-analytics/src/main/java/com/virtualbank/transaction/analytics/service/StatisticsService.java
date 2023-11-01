package com.virtualbank.transaction.analytics.service;

import com.virtualbank.transaction.analytics.service.model.MonetaryAmount;
import com.virtualbank.transaction.analytics.service.model.Transaction;
import com.virtualbank.transaction.analytics.service.model.Statistics;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Component
public class StatisticsService {

    public Statistics calculate(List<Transaction> transactions) {
        return new Statistics(
                calculateTotal(transactions, MonetaryAmount::isSpending),
                calculateTotal(transactions, MonetaryAmount::isIncome),
                calculateTotalByCategory(transactions, MonetaryAmount::isSpending),
                calculateTotalByCategory(transactions, MonetaryAmount::isIncome));
    }

    private static Map<String, List<MonetaryAmount>> calculateTotalByCategory(List<Transaction> transactions,
                                                                              Predicate<MonetaryAmount> filter) {
        return transactions.stream()
                .filter(tr -> filter.test(tr.amount()))
                .collect(groupingBy(Transaction::category)).entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        entry -> groupTotalByCurrency(entry.getValue().stream().map(Transaction::amount))));
    }

    private static List<MonetaryAmount> calculateTotal(List<Transaction> transactions,
                                                       Predicate<MonetaryAmount> filter) {
        return groupTotalByCurrency(transactions.stream()
                .map(Transaction::amount)
                .filter(filter));
    }

    private static List<MonetaryAmount> groupTotalByCurrency(Stream<MonetaryAmount> monetaryAmountStream) {
        return monetaryAmountStream
                .collect(groupingBy(MonetaryAmount::currency)).values()
                .stream()
                .map(toTotalAmount())
                .collect(toList());
    }

    private static Function<List<MonetaryAmount>, MonetaryAmount> toTotalAmount() {
        return amounts -> new MonetaryAmount(
                amounts.stream().mapToLong(MonetaryAmount::amount).sum(),
                amounts.get(0).currency());
    }

}
