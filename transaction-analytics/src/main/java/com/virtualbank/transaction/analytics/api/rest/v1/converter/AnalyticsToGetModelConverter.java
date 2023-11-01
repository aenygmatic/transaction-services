package com.virtualbank.transaction.analytics.api.rest.v1.converter;

import com.virtualbank.transaction.analytics.api.rest.v1.model.GetMonthlyAnalyticsModel;
import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsToGetModelConverter implements Converter<MonthlyAnalytics, GetMonthlyAnalyticsModel> {

    @Override
    public GetMonthlyAnalyticsModel convert(MonthlyAnalytics source) {
        return new GetMonthlyAnalyticsModel(source.getProjections(),
                source.getStatistics());
    }
}
