package ryl.spring.boot.kafka.zipkin.ingestion.event.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ryl.spring.boot.kafka.zipkin.ingestion.api.model.Transaction;
import ryl.spring.boot.kafka.zipkin.ingestion.exception.TransactionPublisherException;

import java.util.concurrent.CompletableFuture;

@Component
public class TransactionsPublisher {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final String topicName;

    @Autowired
    TransactionsPublisher(KafkaTemplate<String, Transaction> kafkaTemplate,
                          @Value("${topic.name.transaction-ingestion}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(Transaction transaction) {
        CompletableFuture<SendResult<String, Transaction>> future =
                kafkaTemplate.send(topicName, transaction);

        future.whenComplete((result, e) -> {
            if (e != null) {
                throw new TransactionPublisherException(e);
            }
        });
    }
}
