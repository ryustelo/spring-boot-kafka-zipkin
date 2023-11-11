package ryl.spring.boot.kafka.zipkin.categorization.event.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ryl.spring.boot.kafka.zipkin.categorization.model.Transaction;
import ryl.spring.boot.kafka.zipkin.categorization.service.TransactionsService;

@Component
public class TransactionsConsumer {
    static final Logger log = LoggerFactory.getLogger(TransactionsConsumer.class);

    private final String topicName;
    private final String groupId;

    private final TransactionsService transactionsService;

    @Autowired
    public TransactionsConsumer(@Value(value = "${topic.name.transaction-ingestion}") String topicName,
                                @Value(value = "${spring.kafka.group-id}") String groupId,
                                TransactionsService transactionsService) {
        this.topicName = topicName;
        this.groupId = groupId;
        this.transactionsService = transactionsService;
    }

    @KafkaListener(topics = "${topic.name.transaction-ingestion}",
            groupId = "${spring.kafka.group-id}")
    public void consume(Transaction transaction) {
        log.info("Transaction successfully consumed: {} from topic: {} and groupId: {}",
                transaction, topicName, groupId);

        transactionsService.categorize(transaction);
    }
}
