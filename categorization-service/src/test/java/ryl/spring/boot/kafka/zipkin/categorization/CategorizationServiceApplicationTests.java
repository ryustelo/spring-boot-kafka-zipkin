package ryl.spring.boot.kafka.zipkin.categorization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ryl.spring.boot.kafka.zipkin.categorization.testconfig.TracingDisabledConfiguration;

@SpringBootTest
@Import(TracingDisabledConfiguration.class)
class CategorizationServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
