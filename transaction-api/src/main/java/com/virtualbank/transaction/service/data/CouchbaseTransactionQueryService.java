package com.virtualbank.transaction.service.data;

import com.virtualbank.transaction.service.TransactionQueryService;
import com.virtualbank.transaction.service.data.couchbase.TransactionRepository;
import com.virtualbank.transaction.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouchbaseTransactionQueryService implements TransactionQueryService {

    private final TransactionRepository repository;

    @Override
    public Optional<Transaction> get(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Transaction> getByUserId(UUID id) {
        return repository.findByUserId(id);
    }
}
