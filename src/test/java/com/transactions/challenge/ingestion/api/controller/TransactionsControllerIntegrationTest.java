package com.transactions.challenge.ingestion.api.controller;

import com.transactions.challenge.ingestion.api.model.Transaction;
import io.restassured.RestAssured;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "${topic.name.transaction-ingestion}",
        bootstrapServersProperty = "spring.kafka.producer.bootstrap-servers")
@DirtiesContext
class TransactionsControllerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Value("${topic.name.transaction-ingestion}")
    private String topicName;


    @Value("${local.server.port}")
    private int port;

    private static final String TRANSACTIONS_INGESTION_URL = "/ingestion-service/transactions";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void givenValidRequest_whenPost_returnsAccepted() {
        Long transactionId = 123L;
        Transaction request = Transaction.builder().transactionId(transactionId).build();

        assertTransactionsIngestionResponse(request, ACCEPTED);

        Consumer<Integer, String> consumer = createConsumer();
        ConsumerRecords<Integer, String> replies = KafkaTestUtils.getRecords(consumer);
        assertThat(replies.count()).isEqualTo(1);
        assertThat(replies.iterator().next().value()).contains(transactionId.toString());
    }

    @Test
    void givenInvalidRequest_whenPost_returnsBadRequest() {
        Transaction request = Transaction.builder().build();

        assertTransactionsIngestionResponse(request, BAD_REQUEST);
    }

    private static void assertTransactionsIngestionResponse(Transaction request, HttpStatus httpStatus) {
        with().contentType(JSON)
                .body(request)
                .when().post(TRANSACTIONS_INGESTION_URL)
                .then()
                .assertThat().statusCode(httpStatus.value());
    }

    private Consumer<Integer, String> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", this.embeddedKafka);
        ConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<Integer, String> consumer = cf.createConsumer();
        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topicName);

        return consumer;
    }
}