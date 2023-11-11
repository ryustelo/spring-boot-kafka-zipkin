package ryl.spring.boot.kafka.zipkin.categorization.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ryl.spring.boot.kafka.zipkin.categorization.event.publisher.TransactionsPublisher;
import ryl.spring.boot.kafka.zipkin.categorization.exception.TransactionPublisherException;
import ryl.spring.boot.kafka.zipkin.categorization.model.Categorizable;
import ryl.spring.boot.kafka.zipkin.categorization.model.CategorizedTransactionFactory;
import ryl.spring.boot.kafka.zipkin.categorization.model.Transaction;

import java.time.Clock;

import static ryl.spring.boot.kafka.zipkin.categorization.configuration.ClockConfiguration.ZONE_ID;

@Service
public class TransactionsService {
    static final Logger log = LoggerFactory.getLogger(TransactionsService.class);

    private final TransactionsPublisher transactionsPublisher;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Autowired
    public TransactionsService(TransactionsPublisher transactionsPublisher,
                               ObjectMapper objectMapper, Clock clock) {
        this.transactionsPublisher = transactionsPublisher;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public void categorize(Transaction transaction) {
        Categorizable categorizedTransaction =
                CategorizedTransactionFactory.create(transaction, clock.instant().atZone(ZONE_ID));

        try {
            transactionsPublisher.publish(categorizedTransaction);
            log.info("Categorized transaction successfully published: {}",
                    objectMapper.writeValueAsString(categorizedTransaction));
        } catch (TransactionPublisherException | JsonProcessingException e) {
            log.error("Error while trying to ingest categorized transaction id: {} with message: {}",
                    transaction.transactionId(), e.getMessage());
        }
    }
}
