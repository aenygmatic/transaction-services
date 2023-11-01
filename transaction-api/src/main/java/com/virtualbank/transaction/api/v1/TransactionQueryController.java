package com.virtualbank.transaction.api.v1;

import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import com.virtualbank.transaction.api.v1.model.GetTransactionModel;
import com.virtualbank.transaction.service.TransactionCommandService;
import com.virtualbank.transaction.service.TransactionQueryService;
import com.virtualbank.transaction.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
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
public class TransactionQueryController {

    private final Converter<Transaction, GetTransactionModel> transactionToRest;
    private final TransactionQueryService transactionQueryService;

    @GetMapping("{id}")
    public ResponseEntity<GetTransactionModel> get(@PathVariable UUID id) {
        return transactionQueryService.get(id)
                .map(transactionToRest::convert)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().<GetTransactionModel>build());
    }

    @GetMapping
    public List<GetTransactionModel> find(@RequestParam Optional<UUID> userId) {
        return userId.map(id -> transactionQueryService.getByUserId(id).stream()
                        .map(transactionToRest::convert)
                        .collect(Collectors.toList()))
                .orElse(transactionQueryService.getAll().stream()
                        .map(transactionToRest::convert)
                        .collect(Collectors.toList()));
    }
}
