package com.virtualbank.transaction.analytics.service.data.couchbase;

import com.virtualbank.transaction.analytics.service.model.MonthlyAnalytics;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface MonthlyAnalyticsRepository extends CouchbaseRepository<MonthlyAnalytics, String> {
}
