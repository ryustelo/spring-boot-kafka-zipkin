package ryl.spring.boot.kafka.zipkin.categorization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static final String ISO_ZONED_DATE_TIME = "YYYY-MM-dd'T'HH:mm:ssZ";

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
