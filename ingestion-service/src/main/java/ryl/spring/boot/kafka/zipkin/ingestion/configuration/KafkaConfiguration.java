package ryl.spring.boot.kafka.zipkin.ingestion.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ryl.spring.boot.kafka.zipkin.ingestion.api.model.Transaction;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    private final KafkaProperties properties;

    @Autowired
    public KafkaConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }
}
