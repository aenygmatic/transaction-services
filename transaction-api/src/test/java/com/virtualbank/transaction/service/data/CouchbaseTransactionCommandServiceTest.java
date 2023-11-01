package com.virtualbank.transaction.service.data;

import com.virtualbank.transaction.service.data.couchbase.TransactionRepository;
import com.virtualbank.transaction.service.model.Transaction;
import com.virtualbank.transaction.service.model.TransactionNotFoundException;
import com.virtualbank.transaction.service.validation.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouchbaseTransactionCommandServiceTest {

    private final Transaction transaction = Transaction.builder()
            .id(UUID.randomUUID())
            .status(Transaction.Status.CREATED)
            .build();

    @Mock
    private TransactionRepository repository;
    @Mock
    private TransactionValidator validator;
    @InjectMocks
    private CouchbaseTransactionCommandService transactionService;


    @Test
    public void testCreate() {
        when(repository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.create(transaction);

        assertEquals(transaction, result);
        assertEquals(Transaction.Status.CREATED, result.getStatus());
        verify(validator).validate(transaction);
        verify(repository).save(transaction);
    }

    @Test
    public void testModifyTransaction() {
        when(repository.existsById(transaction.getId())).thenReturn(true);
        when(repository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.modify(transaction);

        assertEquals(transaction, result);
        assertEquals(Transaction.Status.CREATED, result.getStatus());
        verify(validator).validate(transaction);
        verify(repository).save(transaction);
    }

    @Test
    public void testModifyNonExistingTransaction() {
        when(repository.existsById(transaction.getId())).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.modify(transaction));

        verify(validator).validate(transaction);
        verify(repository, never()).save(transaction);
    }


    @Test
    public void testDeleteTransaction() {
        when(repository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(repository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.delete(transaction.getId());

        assertEquals(transaction, result);
        assertEquals(Transaction.Status.DELETED, result.getStatus());
        verify(repository).save(transaction);
    }

    @Test
    public void testDeleteNonExistingTransaction() {
        when(repository.findById(transaction.getId())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(transaction.getId()));

        verify(repository, never()).save(transaction);
    }
}