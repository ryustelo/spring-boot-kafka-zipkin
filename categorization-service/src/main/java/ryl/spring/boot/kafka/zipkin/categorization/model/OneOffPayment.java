package ryl.spring.boot.kafka.zipkin.categorization.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static ryl.spring.boot.kafka.zipkin.categorization.configuration.ClockConfiguration.ISO_ZONED_DATE_TIME;
import static ryl.spring.boot.kafka.zipkin.categorization.model.CategoryType.ONE_OFF_PAYMENT;

final class OneOffPayment extends CategorizedTransaction {

    @JsonFormat(pattern = ISO_ZONED_DATE_TIME)
    private final ZonedDateTime chargeDate;
    private final BigDecimal amount;

    OneOffPayment(Transaction transaction, ZonedDateTime now) {
        super(transaction);
        this.chargeDate = now;
        this.amount = BigDecimal.valueOf(30.0);
    }

    @Override
    public CategoryType calculateCategory() {
        return ONE_OFF_PAYMENT;
    }

    public ZonedDateTime getChargeDate() {
        return chargeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
