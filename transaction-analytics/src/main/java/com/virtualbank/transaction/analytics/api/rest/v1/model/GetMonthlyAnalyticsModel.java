package com.virtualbank.transaction.analytics.api.rest.v1.model;

import com.virtualbank.transaction.analytics.service.model.Projections;
import com.virtualbank.transaction.analytics.service.model.Statistics;

public record GetMonthlyAnalyticsModel(Projections projections,
                                       Statistics statistics) {
}
