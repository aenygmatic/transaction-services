package com.virtualbank.transaction.service.event;

import com.virtualbank.transaction.service.TransactionCommandService;
import com.virtualbank.transaction.service.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaEventSenderTransactionService implements TransactionCommandService {

    private final TransactionCommandService service;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final String topic;

    @Override
    public Transaction create(Transaction transaction) {
        Transaction result = service.create(transaction);
        send(new TransactionEvent(TransactionEvent.Type.CREATED, result));
        return result;
    }

    @Override
    public Transaction modify(Transaction transaction) {
        Transaction result = service.modify(transaction);
        send(new TransactionEvent(TransactionEvent.Type.UPDATED, result));
        return result;
    }

    @Override
    public Transaction delete(UUID id) {
        Transaction result = service.delete(id);
        send(new TransactionEvent(TransactionEvent.Type.DELETED, result));
        return result;
    }

    private void send(TransactionEvent event) {
        kafkaTemplate.send(topic, event.content().getUserId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (Objects.nonNull(ex)) {
                        log.error("Failed to send transaction event! topic=" + topic + " event=" + event, ex);
                    } else {
                        log.debug("Transaction event sent! topic=" + topic + " event=" + event);
                    }
                });
    }

}
