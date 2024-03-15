package com.virtualbank.transaction.analytics.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualbank.transaction.analytics.api.event.EventConsumer;
import com.virtualbank.transaction.analytics.api.event.model.TransactionEvent;

public interface MessagingSteps {

    default void givenMessageSent(TransactionEvent message) {
        try {
            getEventConsumer().onTransactionEvent(getObjectMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    EventConsumer getEventConsumer();

    ObjectMapper getObjectMapper();
}
