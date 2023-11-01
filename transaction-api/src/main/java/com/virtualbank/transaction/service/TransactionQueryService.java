package com.virtualbank.transaction.service;

import com.virtualbank.transaction.service.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionQueryService {

    Optional<Transaction> get(UUID id);

    List<Transaction> getAll();

    List<Transaction> getByUserId(UUID id);
}
