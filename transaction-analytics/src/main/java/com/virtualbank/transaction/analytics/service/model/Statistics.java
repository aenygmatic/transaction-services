package com.virtualbank.transaction.analytics.service.model;

import java.util.List;
import java.util.Map;

public record Statistics(List<MonetaryAmount> totalSpending,
                         List<MonetaryAmount> totalIncome,
                         Map<String, List<MonetaryAmount>> spendingByCategory,
                         Map<String, List<MonetaryAmount>> incomeByCategory) {
}
