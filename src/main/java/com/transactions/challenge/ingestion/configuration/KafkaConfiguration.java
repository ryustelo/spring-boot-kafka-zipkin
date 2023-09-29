package com.transactions.challenge.ingestion.configuration;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

//@Configuration
public class KafkaConfiguration {

//    private final String bootstrapAddress;
//
//    KafkaConfiguration(@Value("${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
//        this.bootstrapAddress = bootstrapAddress;
//    }
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        Map<String, Object> properties = new HashMap<>();
//
//        properties.put(BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress);
//        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(properties);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
}
