package com.virtualbank.transaction.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.time.Instant;
import java.util.UUID;

@Data
@Document
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private UUID id;
    @Field
    private UUID userId;
    @Field
    private String description;
    @Field
    private String category;
    @Field
    private long amount;
    @Field
    private String currency;
    @Field
    private Instant timestamp;
    @Field
    private Status status;

    public enum Status {
        CREATED, DELETED;
    }

}
