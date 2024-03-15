package com.virtualbank.transaction.analytics.steps;

import com.couchbase.client.java.Cluster;
import com.virtualbank.transaction.analytics.service.data.couchbase.CouchbaseConfig;

public interface DatabaseSteps {

    default void givenCleanDatabase() {
        getCluster().query("DELETE from `analytics`");
    }

    Cluster getCluster();
}
