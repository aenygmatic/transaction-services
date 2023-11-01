package com.virtualbank.transaction.service.data.couchbase;

import com.virtualbank.transaction.service.model.Transaction;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CouchbaseRepository<Transaction, UUID> {

    @Query("#{#n1ql.selectEntity} WHERE userId = $1")
    List<Transaction> findByUserId(UUID id);
}
