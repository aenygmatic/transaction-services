package com.virtualbank.transaction.analytics.service;

import com.virtualbank.transaction.analytics.service.model.Transaction;

import java.util.UUID;

public interface AnalyticsChangeService {

    void addTransaction(UUID userId, Transaction transaction);

    void updateTransaction(UUID userId, Transaction transaction);

    void deleteTransaction(UUID userId, Transaction transaction);
}
