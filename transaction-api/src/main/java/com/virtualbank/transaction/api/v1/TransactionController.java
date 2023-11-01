package com.virtualbank.transaction.api.v1;

import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import com.virtualbank.transaction.api.v1.model.GetTransactionModel;
import com.virtualbank.transaction.service.model.Transaction;
import com.virtualbank.transaction.service.TransactionQueryService;
import com.virtualbank.transaction.service.TransactionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final Converter<CreateUpdateTransactionModel, Transaction> restToTransaction;
    private final TransactionCommandService transactionCommandService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateUpdateTransactionModel data) {
        Transaction transaction = transactionCommandService.create(restToTransaction.convert(data));
        return ResponseEntity.created(URI.create("/transaction/" + transaction.getId())).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<String> modify(@PathVariable UUID id, @RequestBody CreateUpdateTransactionModel data) {
        Transaction transaction = restToTransaction.convert(data);
        transaction.setId(id);
        transactionCommandService.modify(transaction);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        transactionCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
