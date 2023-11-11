package ryl.spring.boot.kafka.zipkin.categorization.testconfig;

import brave.Tracing;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TracingDisabledConfiguration {

    @Bean
    Tracing tracing() {
        return Tracing.newBuilder().build();
    }
}
