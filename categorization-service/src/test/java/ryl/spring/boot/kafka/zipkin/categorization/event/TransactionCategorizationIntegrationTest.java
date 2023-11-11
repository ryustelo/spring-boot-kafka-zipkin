package ryl.spring.boot.kafka.zipkin.categorization.event;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import ryl.spring.boot.kafka.zipkin.categorization.testconfig.TracingDisabledConfiguration;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@EmbeddedKafka(topics = {"${topic.name.transaction-ingestion}",
        "${topic.name.transaction-categorization}"})
@TestInstance(PER_CLASS)
@Import(TracingDisabledConfiguration.class)
@DirtiesContext
class TransactionCategorizationIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Value("${topic.name.transaction-ingestion}")
    private String ingestionTopicName;

    @Value("${topic.name.transaction-categorization}")
    private String categorizationTopicName;

    private KafkaTemplate<String, String> producer;
    private Consumer<Integer, String> consumer;

    private static final String REQUEST_TEMPLATE = """
            {"transactionId": %s}""";

    @BeforeAll
    void beforeAll() {
        producer = createProducer();
        consumer = createConsumer();
    }

    @ParameterizedTest
    @MethodSource
    void givenIngestedTransaction_whenConsumed_thenCategorizedTransactionIsPublished(
            Long transactionId, String expectedCategoryType) {
        producer.send(ingestionTopicName, REQUEST_TEMPLATE.formatted(transactionId));

        ConsumerRecords<Integer, String> replies = KafkaTestUtils.getRecords(consumer);

        assertThat(replies.count()).isEqualTo(1);
        String consumedTransaction = replies.iterator().next().value();
        assertThat(consumedTransaction).contains(transactionId.toString());
        assertThat(consumedTransaction).contains(expectedCategoryType);
    }

    private static Stream<Arguments> givenIngestedTransaction_whenConsumed_thenCategorizedTransactionIsPublished() {
        return Stream.of(
                Arguments.of(3L, "ONE_OFF_PAYMENT"),
                Arguments.of(4L, "RECURRING_PAYMENT"),
                Arguments.of(5L, "REFUND")
        );
    }

    private KafkaTemplate<String, String> createProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(this.embeddedKafka);
        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps);

        return new KafkaTemplate<>(pf);
    }

    private Consumer<Integer, String> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", this.embeddedKafka);
        ConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<Integer, String> consumer = cf.createConsumer();
        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, categorizationTopicName);

        return consumer;
    }
}
