package com.virtualbank.transaction.steps;

import com.virtualbank.transaction.api.v1.model.CreateUpdateTransactionModel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.with;

public interface TransactionSteps {


    default Response whenTransactionIsSearchedById(String transactionId) {
        return when()
                .request("GET", "/transactions/" + transactionId);
    }

    default String givenTransactionCreated(CreateUpdateTransactionModel createTransactionRequest) {
        var createResponse = with()
                .body(createTransactionRequest)
                .contentType(ContentType.JSON)
                .request("POST", "/transactions");
        createResponse.then()
                .statusCode(201);
        return getIdFromLocationHeader(createResponse);
    }

    default Response whenTransactionDeletedById(String id) {
        return with()
                .request("DELETE", "/transactions/" + id);
    }

    default String getIdFromLocationHeader(Response createResponse) {
        return createResponse.getHeader("location").split("/")[2];
    }

    default Response whenTransactionIsUpdated(String id, CreateUpdateTransactionModel modifiedTransaction) {
        return with()
                .body(modifiedTransaction)
                .contentType(ContentType.JSON)
                .request("PUT", "/transactions/" + id);
    }
}
