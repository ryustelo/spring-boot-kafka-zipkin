package com.transactions.challenge.ingestion.repository;

import com.transactions.challenge.ingestion.exception.TransactionPublisherException;
import com.transactions.challenge.ingestion.model.TransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

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

    public void publishMessage(TransactionRequest transaction) {

        try {
            kafkaTemplate.send(topicName, transaction.toString()).get();
        } catch (Exception e) {
            throw new TransactionPublisherException();
        }

//        future.whenComplete((result, ex) -> {
//            if (nonNull(ex)) {
//                throw new RuntimeException(ex.getMessage());
//            }
//        });
    }
}
