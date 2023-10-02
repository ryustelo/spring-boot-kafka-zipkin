package com.transactions.challenge.ingestion.service;

import com.transactions.challenge.ingestion.api.model.TransactionRequest;
import com.transactions.challenge.ingestion.exception.TransactionPublisherException;
import com.transactions.challenge.ingestion.repository.TransactionsPublisher;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {
    static final Logger log = LoggerFactory.getLogger(TransactionsService.class);

    private final TransactionsPublisher transactionsPublisher;

    @Autowired
    public TransactionsService(TransactionsPublisher transactionsPublisher) {
        this.transactionsPublisher = transactionsPublisher;
    }

    @Observed(contextualName = "ingest-transaction")
    public void ingest(TransactionRequest transactionRequest) {
        log.info("Transaction requested to ingest: {}", transactionRequest);

        try {
            transactionsPublisher.publishMessage(transactionRequest);
        } catch (Exception e) {
            log.error("Error while trying to ingest transaction: {}", transactionRequest);
            throw new TransactionPublisherException(e);
        }
        log.info("Transaction successfully ingested: {}", transactionRequest);
    }
}
