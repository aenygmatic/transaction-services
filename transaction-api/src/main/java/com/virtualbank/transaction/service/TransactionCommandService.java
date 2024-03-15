package com.virtualbank.transaction.service;

import com.virtualbank.transaction.service.model.Transaction;

import java.util.UUID;

public interface TransactionCommandService {

    Transaction create(Transaction transaction);

    Transaction modify(Transaction transaction);

    Transaction delete(UUID id);

}
