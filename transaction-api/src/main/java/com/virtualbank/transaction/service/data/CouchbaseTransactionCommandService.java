package com.virtualbank.transaction.service.data;

import com.virtualbank.transaction.service.TransactionCommandService;
import com.virtualbank.transaction.service.data.couchbase.TransactionRepository;
import com.virtualbank.transaction.service.model.Transaction;
import com.virtualbank.transaction.service.model.TransactionNotFoundException;
import com.virtualbank.transaction.service.validation.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouchbaseTransactionCommandService implements TransactionCommandService {

    private final TransactionRepository repository;
    private final TransactionValidator validator;

    @Override
    public Transaction create(Transaction transaction) {
        transaction.setStatus(Transaction.Status.CREATED);
        validator.validate(transaction);
        return repository.save(transaction);
    }

    @Override
    public Transaction modify(Transaction transaction) {
        transaction.setStatus(Transaction.Status.CREATED);
        validator.validate(transaction);
        if (repository.existsById(transaction.getId())) {
            return repository.save(transaction);
        } else {
            throw new TransactionNotFoundException(transaction.getId());
        }
    }

    @Override
    public Transaction delete(UUID id) {
        return repository.findById(id).map(t -> {
                    t.setStatus(Transaction.Status.DELETED);
                    return repository.save(t);
                })
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }
}
