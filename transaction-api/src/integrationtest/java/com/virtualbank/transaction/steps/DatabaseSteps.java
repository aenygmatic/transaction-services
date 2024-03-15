package com.virtualbank.transaction.steps;

import com.couchbase.client.java.Cluster;
import com.virtualbank.transaction.service.data.couchbase.CouchbaseConfig;

public interface DatabaseSteps {

    default void givenCleanDatabase() {
        getCluster().query("DELETE from `transactions`");
    }

    Cluster getCluster();
}
