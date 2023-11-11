package ryl.spring.boot.kafka.zipkin.ingestion.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ryl.spring.boot.kafka.zipkin.ingestion.api.model.Transaction;
import ryl.spring.boot.kafka.zipkin.ingestion.event.publisher.TransactionsPublisher;
import ryl.spring.boot.kafka.zipkin.ingestion.exception.TransactionPublisherException;

@Service
public class TransactionsService {
    static final Logger log = LoggerFactory.getLogger(TransactionsService.class);

    private final TransactionsPublisher transactionsPublisher;

    @Autowired
    public TransactionsService(TransactionsPublisher transactionsPublisher) {
        this.transactionsPublisher = transactionsPublisher;
    }

    @Observed(contextualName = "ingest-transaction")
    @Async
    public void ingest(Transaction transaction) {
        log.info("Transaction requested to ingest: {}", transaction);

        try {
            transactionsPublisher.publish(transaction);
            log.info("Transaction successfully ingested: {}", transaction);
        } catch (TransactionPublisherException e) {
            log.error("Error while trying to ingest transaction: {} with message: {}",
                    transaction, e.getMessage());
        }
    }
}
