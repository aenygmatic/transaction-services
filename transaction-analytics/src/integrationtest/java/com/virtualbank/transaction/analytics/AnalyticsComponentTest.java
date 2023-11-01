package com.virtualbank.transaction.analytics;

import com.couchbase.client.java.Cluster;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualbank.transaction.analytics.api.event.EventConsumer;
import com.virtualbank.transaction.analytics.api.event.model.TransactionEvent;
import com.virtualbank.transaction.analytics.service.data.couchbase.CouchbaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.UUID;

import static io.restassured.RestAssured.when;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransactionAnalyticsApplication.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "server.port=8080")
public class AnalyticsComponentTest {

    @Autowired
    private Cluster cluster;
    @Autowired
    private CouchbaseConfig couchbaseConfig;
    @Autowired
    private EventConsumer eventConsumer;
    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();
    private final Instant now = Instant.now();

    @BeforeEach
    public void cleanup() {
        cluster.query("DELETE from `analytics`");
    }

    @Test
    public void getAnalyticsWhenNotFound() {
        when()
                .request("GET", "/analytics?userId=" + UUID.randomUUID())
                .then()
                .statusCode(404);
    }

    @Test
    public void testingNewAnalyticsWithoutProjections() {
        send(newCreateTransactionEvent(userId, "restaurant", -150, "EUR", now));
        send(newCreateTransactionEvent(userId, "restaurant", -100, "EUR", now));
        send(newCreateTransactionEvent(userId, "grocery", -300, "EUR", now));
        send(newCreateTransactionEvent(userId, "salary", 9000, "EUR", now));


        when()
                .request("GET", "/analytics?userId=" + userId)
                .then()
                .statusCode(200)
                .assertThat()
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
        send(newCreateTransactionEvent(userId, "restaurant", -150, "EUR", lastMonth));
        send(newCreateTransactionEvent(userId, "restaurant", -100, "EUR", lastMonth));
        send(newCreateTransactionEvent(userId, "grocery", -300, "EUR", lastMonth));
        send(newCreateTransactionEvent(userId, "salary", 9000, "EUR", lastMonth));
        send(newCreateTransactionEvent(userId, "salary", 8000, "EUR", thisMonth));


        when()
                .request("GET", "/analytics?userId=" + userId + "&year=2023&month=10")
                .then()
                .statusCode(200)
                .assertThat()
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

    private static TransactionEvent newCreateTransactionEvent(UUID userId,
                                                              String category,
                                                              int amount,
                                                              String currency,
                                                              Instant timestamp) {
        return new TransactionEvent(TransactionEvent.Type.CREATED,
                new TransactionEvent.Data(
                        UUID.randomUUID(),
                        userId,
                        "dec",
                        category,
                        amount,
                        currency,
                        timestamp,
                        "CREATED"
                ));
    }

    private void send(TransactionEvent message) {
        try {
            eventConsumer.onTransactionEvent(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
