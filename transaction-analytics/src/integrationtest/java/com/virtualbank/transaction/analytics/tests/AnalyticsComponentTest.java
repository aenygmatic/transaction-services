package com.virtualbank.transaction.analytics.tests;

import com.virtualbank.transaction.analytics.IntegrationTest;
import com.virtualbank.transaction.analytics.TestDataHelper;
import com.virtualbank.transaction.analytics.steps.AnalyticsSteps;
import com.virtualbank.transaction.analytics.steps.DatabaseSteps;
import com.virtualbank.transaction.analytics.steps.MessagingSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.hamcrest.core.IsEqual.equalTo;


public class AnalyticsComponentTest extends IntegrationTest implements DatabaseSteps, MessagingSteps, AnalyticsSteps, TestDataHelper {


    private final UUID userId = UUID.randomUUID();
    private final Instant now = Instant.now();

    @BeforeEach
    public void cleanup() {
        givenCleanDatabase();
    }

    @Test
    public void getAnalyticsWhenNotFound() {
        whenAnalyticsAreQueriedFor(UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void testingNewAnalyticsWithoutProjections() {
        givenMessageSent(
                newCreateTransactionEvent(userId, "restaurant", -150, "EUR", now));
        givenMessageSent(
                newCreateTransactionEvent(userId, "restaurant", -100, "EUR", now));
        givenMessageSent(
                newCreateTransactionEvent(userId, "grocery", -300, "EUR", now));
        givenMessageSent(
                newCreateTransactionEvent(userId, "salary", 9000, "EUR", now));


        whenAnalyticsAreQueriedFor(userId)
                .then()
                .statusCode(200)
                .body("projections.projectedSpending", equalTo(emptyList()))
                .body("projections.projectedIncomes", equalTo(emptyList()))

                .body("statistics.totalSpending[0].amount", equalTo(-550))
                .body("statistics.totalSpending[0].currency", equalTo("EUR"))

                .body("statistics.totalIncome[0].amount", equalTo(9000))
                .body("statistics.totalIncome[0].currency", equalTo("EUR"))

                .body("statistics.spendingByCategory.grocery[0].amount", equalTo(-300))
                .body("statistics.spendingByCategory.grocery[0].currency", equalTo("EUR"))
                .body("statistics.spendingByCategory.restaurant[0].amount", equalTo(-250))
                .body("statistics.spendingByCategory.restaurant[0].currency", equalTo("EUR"))

                .body("statistics.incomeByCategory.salary[0].amount", equalTo(9000))
                .body("statistics.incomeByCategory.salary[0].currency", equalTo("EUR"));
    }


    @Test
    public void testingNewAnalyticsWithProjections() {
        Instant lastMonth = LocalDateTime.of(2023, 9, 15, 0, 0).toInstant(ZoneOffset.UTC);
        Instant thisMonth = LocalDateTime.of(2023, 10, 15, 0, 0).toInstant(ZoneOffset.UTC);

        givenMessageSent(
                newCreateTransactionEvent(userId, "restaurant", -150, "EUR", lastMonth));
        givenMessageSent(
                newCreateTransactionEvent(userId, "restaurant", -100, "EUR", lastMonth));
        givenMessageSent(
                newCreateTransactionEvent(userId, "grocery", -300, "EUR", lastMonth));
        givenMessageSent(
                newCreateTransactionEvent(userId, "salary", 9000, "EUR", lastMonth));
        givenMessageSent(
                newCreateTransactionEvent(userId, "salary", 8000, "EUR", thisMonth));


        whenAnalyticsAreQueriedFor(userId, "2023", "10")
                .then()
                .statusCode(200)
                .body("projections.projectedSpending[0].amount", equalTo(-550))
                .body("projections.projectedSpending[0].currency", equalTo("EUR"))
                .body("projections.projectedIncomes[0].amount", equalTo(9000))
                .body("projections.projectedIncomes[0].currency", equalTo("EUR"))

                .body("statistics.totalSpending", equalTo(emptyList()))

                .body("statistics.totalIncome[0].amount", equalTo(8000))
                .body("statistics.totalIncome[0].currency", equalTo("EUR"))

                .body("statistics.spendingByCategory", equalTo(Collections.emptyMap()))

                .body("statistics.incomeByCategory.salary[0].amount", equalTo(8000))
                .body("statistics.incomeByCategory.salary[0].currency", equalTo("EUR"));
    }
}
