package ryl.spring.boot.kafka.zipkin.categorization.configuration;

import brave.Tracing;
import brave.kafka.clients.KafkaTracing;
import ryl.spring.boot.kafka.zipkin.categorization.model.Categorizable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    private final KafkaProperties properties;

    @Autowired
    public KafkaConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaTracing kafkaTracing(Tracing tracing) {
        return KafkaTracing.create(tracing);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(KafkaTracing kafkaTracing) {
        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(properties.buildConsumerProperties());
        consumerFactory.addPostProcessor(kafkaTracing::consumer);

        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Categorizable> producerFactory() {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, Categorizable> kafkaTemplate() {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }
}
