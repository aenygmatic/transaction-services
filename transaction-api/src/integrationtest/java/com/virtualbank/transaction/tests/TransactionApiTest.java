package com.virtualbank.transaction.tests;

import com.virtualbank.transaction.IntegrationTest;
import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import com.virtualbank.transaction.service.model.Transaction;
import com.virtualbank.transaction.steps.DatabaseSteps;
import com.virtualbank.transaction.steps.TransactionSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static com.virtualbank.transaction.TransactionMatchers.atSameTime;
import static org.hamcrest.Matchers.equalTo;

public class TransactionApiTest extends IntegrationTest implements DatabaseSteps, TransactionSteps {

    private final CreateUpdateTransactionModel createTransaction = new CreateUpdateTransactionModel(
            UUID.randomUUID(),
            "test transaction",
            "shopping",
            -1000,
            "EUR",
            Instant.now()
    );


    @BeforeEach
    public void cleanup() {
        givenCleanDatabase();
    }

    @Test
    public void getTransactionByIdWhenTransactionDoesNotExists() {
        whenTransactionIsSearchedById(UUID.randomUUID().toString())
                .then()
                .statusCode(404);
    }

    @Test
    public void createAndGetTransaction() {
        String id = givenTransactionCreated(createTransaction);

        whenTransactionIsSearchedById(id)
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
        String id = givenTransactionCreated(createTransaction);

        var modifiedTransaction = new CreateUpdateTransactionModel(
                createTransaction.userId(),
                "test transaction",
                "rent",
                -2000,
                "GBP",
                Instant.now());

        whenTransactionIsUpdated(id, modifiedTransaction)
                .then()
                .statusCode(200);

        whenTransactionIsSearchedById(id)
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
        String id = givenTransactionCreated(createTransaction);


        whenTransactionDeletedById(id)
                .then()
                .statusCode(204);

        whenTransactionIsSearchedById(id)
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
}
