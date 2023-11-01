package com.virtualbank.transaction.service.data.couchbase;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Getter
@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${data.couchbase.connection}")
    private String connectionString;
    @Value("${data.couchbase.user}")
    private String user;
    @Value("${data.couchbase.password}")
    private String password;
    @Value("${data.couchbase.bucket}")
    private String bucket;


    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucket;
    }
}
