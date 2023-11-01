package com.virtualbank.transaction.analytics.service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;

@Data
@Builder
@Document
public class MonthlyAnalytics {

    @Id
    private String id;
    @Field
    private List<Transaction> transactions;
    @Field
    private Projections projections;
    @Field
    private Statistics statistics;

}
