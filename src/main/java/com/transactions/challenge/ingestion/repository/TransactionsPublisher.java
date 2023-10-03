package com.transactions.challenge.ingestion.repository;

import com.transactions.challenge.ingestion.api.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class TransactionsPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    @Autowired
    TransactionsPublisher(KafkaTemplate<String, String> kafkaTemplate,
                          @Value("${topic.name.transaction-ingestion}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(Transaction transaction)
            throws ExecutionException, InterruptedException {

        kafkaTemplate.send(topicName, transaction.toString()).get();
    }
}
