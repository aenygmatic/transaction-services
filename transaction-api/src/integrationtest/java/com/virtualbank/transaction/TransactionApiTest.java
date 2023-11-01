package com.virtualbank.transaction;

import com.couchbase.client.java.Cluster;
import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import com.virtualbank.transaction.service.data.couchbase.CouchbaseConfig;
import com.virtualbank.transaction.service.model.Transaction;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransactionsApplication.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "event.enabled=true")
public class TransactionApiTest {

    private final CreateUpdateTransactionModel createTransaction = new CreateUpdateTransactionModel(
            UUID.randomUUID(),
            "test transaction",
            "shopping",
            -1000,
            "EUR",
            Instant.now()
    );
    @Autowired
    private Cluster cluster;
    @Autowired
    private CouchbaseConfig couchbaseConfig;

    @BeforeEach
    public void cleanup() {
        cluster.query("DELETE from `transactions`");
    }

    @Test
    public void getTransactionByIdWhenTransactionDoesNotExists() {
        when()
                .request("GET", "/transactions/" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void createAndGetTransaction() {
        var createResponse = with()
                .body(createTransaction)
                .contentType(ContentType.JSON)
                .request("POST", "/transactions");
        createResponse.then()
                .statusCode(201);

        String id = getIdFromLocationHeader(createResponse);

        when()
                .request("GET", "/transactions/" + id)
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(id))
                .body("userId", equalTo(createTransaction.userId().toString()))
                .body("description", equalTo(createTransaction.description()))
                .body("category", equalTo(createTransaction.category()))
                .body("amount", equalTo((int) createTransaction.amount()))
                .body("currency", equalTo(createTransaction.currency()))
                .body("timestamp", atSameTime(createTransaction.timestamp()))
                .body("status", equalTo(Transaction.Status.CREATED.toString()));
    }

    @Test
    public void createAndModifyTransaction() {
        var createResponse = with()
                .body(createTransaction)
                .contentType(ContentType.JSON)
                .request("POST", "/transactions");
        createResponse.then()
                .statusCode(201);

        String id = getIdFromLocationHeader(createResponse);

        var modifiedTransaction = new CreateUpdateTransactionModel(
                createTransaction.userId(),
                "test transaction",
                "rent",
                -2000,
                "GBP",
                Instant.now());

        with()
                .body(modifiedTransaction)
                .contentType(ContentType.JSON)
                .request("PUT", "/transactions/" + id)
                .then()
                .statusCode(200);

        when()
                .request("GET", "/transactions/" + id)
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(id))
                .body("userId", equalTo(modifiedTransaction.userId().toString()))
                .body("description", equalTo(modifiedTransaction.description()))
                .body("category", equalTo(modifiedTransaction.category()))
                .body("amount", equalTo((int) modifiedTransaction.amount()))
                .body("currency", equalTo(modifiedTransaction.currency()))
                .body("timestamp", atSameTime(modifiedTransaction.timestamp()))
                .body("status", equalTo(Transaction.Status.CREATED.toString()));
    }

    @Test
    public void createAndDeleteTransaction() {
        var createResponse = with()
                .body(createTransaction)
                .contentType(ContentType.JSON)
                .request("POST", "/transactions");
        createResponse.then()
                .statusCode(201);
        String id = getIdFromLocationHeader(createResponse);


        with()
                .request("DELETE", "/transactions/" + id)
                .then()
                .statusCode(204);

        when()
                .request("GET", "/transactions/" + id)
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(id))
                .body("userId", equalTo(createTransaction.userId().toString()))
                .body("description", equalTo(createTransaction.description()))
                .body("category", equalTo(createTransaction.category()))
                .body("amount", equalTo((int) createTransaction.amount()))
                .body("currency", equalTo(createTransaction.currency()))
                .body("timestamp", atSameTime(createTransaction.timestamp()))
                .body("status", equalTo(Transaction.Status.DELETED.toString()));
    }

    private static String getIdFromLocationHeader(Response createResponse) {
        return createResponse.getHeader("location").split("/")[2];
    }

    public static org.hamcrest.Matcher<String> atSameTime(Instant operand) {
        //TODO: take care of non 'Z' timezones
        return new IsEqual<>(operand.toString().substring(0, 22)) {
            @Override
            public boolean matches(Object actualValue) {
                return super.matches(actualValue.toString().substring(0, 22));
            }
        };
    }
}
