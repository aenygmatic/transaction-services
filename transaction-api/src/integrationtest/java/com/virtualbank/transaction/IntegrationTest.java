package com.virtualbank.transaction;


import com.couchbase.client.java.Cluster;
import com.virtualbank.transaction.service.data.couchbase.CouchbaseConfig;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Getter
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransactionsApplication.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "event.enabled=true")
public abstract class IntegrationTest {

    @Autowired
    private Cluster cluster;
    @Autowired
    private CouchbaseConfig couchbaseConfig;
}
