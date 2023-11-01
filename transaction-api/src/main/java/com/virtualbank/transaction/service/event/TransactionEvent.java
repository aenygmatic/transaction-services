package com.virtualbank.transaction.service.event;

import com.virtualbank.transaction.service.model.Transaction;

public record TransactionEvent(Type type, Transaction content) {

    public enum Type {
        CREATED, UPDATED, DELETED;
    }
}
