package com.virtualbank.transaction.analytics.service.model;

import java.util.Collections;
import java.util.List;


public record Projections(List<MonetaryAmount> projectedSpending,
                          List<MonetaryAmount> projectedIncomes) {

    private static final Projections EMPTY = new Projections(Collections.emptyList(), Collections.emptyList());

    public static Projections empty() {
        return EMPTY;
    }
}
