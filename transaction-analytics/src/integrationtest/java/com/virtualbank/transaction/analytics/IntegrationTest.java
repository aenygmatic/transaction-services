package com.virtualbank.transaction.analytics;

import com.couchbase.client.java.Cluster;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualbank.transaction.analytics.api.event.EventConsumer;
import com.virtualbank.transaction.analytics.service.data.couchbase.CouchbaseConfig;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Getter
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = TransactionAnalyticsApplication.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8080")
public abstract class IntegrationTest {

    @Autowired
    private Cluster cluster;
    @Autowired
    private CouchbaseConfig couchbaseConfig;
    @Autowired
    private EventConsumer eventConsumer;
    @Autowired
    private ObjectMapper objectMapper;

}
