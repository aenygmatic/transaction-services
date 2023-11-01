package com.virtualbank.transaction.analytics.api.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualbank.transaction.analytics.api.event.model.TransactionEvent;
import com.virtualbank.transaction.analytics.service.AnalyticsChangeService;
import com.virtualbank.transaction.analytics.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final AnalyticsChangeService analyticsChangeService;
    private final Converter<TransactionEvent.Data, Transaction> eventToTransactionConverter;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${event.kafka.topic}", groupId = "${event.kafka.group-id}")
    public void onTransactionEvent(@Payload String body) {
        log.debug("Transaction event received! event=" + body);

        var message = parseJson(body);
        var transaction = eventToTransactionConverter.convert(message.content());
        var userId = message.content().userId();

        switch (message.type()) {
            case CREATED -> analyticsChangeService.addTransaction(userId, transaction);
            case UPDATED -> analyticsChangeService.updateTransaction(userId, transaction);
            case DELETED -> analyticsChangeService.deleteTransaction(userId, transaction);
            default -> throw new IllegalArgumentException("Unhandled event type! eventType=" + message.type());
        }
        log.debug("Transaction event processed! event=" + message);
    }

    private TransactionEvent parseJson(String body) {
        TransactionEvent message;
        try {
            message = objectMapper.readValue(body, TransactionEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
