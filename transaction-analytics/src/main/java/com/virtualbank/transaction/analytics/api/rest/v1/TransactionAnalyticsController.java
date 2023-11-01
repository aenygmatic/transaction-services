package com.virtualbank.transaction.analytics.api.rest.v1;

import com.virtualbank.transaction.analytics.api.rest.v1.model.GetMonthlyAnalyticsModel;
import com.virtualbank.transaction.analytics.service.AnalyticsQueryService;
import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class TransactionAnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;
    private final Converter<MonthlyAnalytics, GetMonthlyAnalyticsModel> analyticsToGetModel;

    @GetMapping
    public ResponseEntity<GetMonthlyAnalyticsModel> find(@RequestParam UUID userId,
                                                         @RequestParam Optional<Integer> year,
                                                         @RequestParam Optional<Integer> month) {
        if (year.isPresent() && month.isPresent()) {
            return analyticsQueryService.getFor(userId, year.get(), month.get())
                    .map(analyticsToGetModel::convert)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return analyticsQueryService.getForNow(userId)
                    .map(analyticsToGetModel::convert)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    }
}
