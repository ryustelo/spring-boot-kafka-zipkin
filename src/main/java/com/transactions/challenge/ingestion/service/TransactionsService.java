package com.transactions.challenge.ingestion.service;

import com.transactions.challenge.ingestion.model.TransactionRequest;
import com.transactions.challenge.ingestion.repository.TransactionsPublisher;
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

    public void ingest(TransactionRequest transactionRequest) {
        transactionsPublisher.publishMessage(transactionRequest);
    }
}
