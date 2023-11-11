package ryl.spring.boot.kafka.zipkin.categorization.event.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ryl.spring.boot.kafka.zipkin.categorization.exception.TransactionPublisherException;
import ryl.spring.boot.kafka.zipkin.categorization.model.Categorizable;

import java.util.concurrent.CompletableFuture;

@Component
public class TransactionsPublisher {

    private final KafkaTemplate<String, Categorizable> kafkaTemplate;
    private final String topicName;

    @Autowired
    TransactionsPublisher(KafkaTemplate<String, Categorizable> kafkaTemplate,
                          @Value("${topic.name.transaction-categorization}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(Categorizable transaction) {
        CompletableFuture<SendResult<String, Categorizable>> future =
                kafkaTemplate.send(topicName, transaction);

        future.whenComplete((result, e) -> {
            if (e != null) {
                throw new TransactionPublisherException(e);
            }
        });
    }
}
