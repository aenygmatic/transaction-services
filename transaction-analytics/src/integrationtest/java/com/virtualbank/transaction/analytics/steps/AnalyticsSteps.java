package com.virtualbank.transaction.analytics.steps;

import io.restassured.response.Response;

import java.util.UUID;

import static io.restassured.RestAssured.when;

public interface AnalyticsSteps {

    default Response whenAnalyticsAreQueriedFor(UUID user) {
        return when()
                .request("GET", "/analytics?userId=" + user);
    }

    default Response whenAnalyticsAreQueriedFor(UUID user, String year, String month) {
        return when()
                .request("GET", "/analytics?userId=" + user + "&year=" + year + "&month=" + month);
    }
}
