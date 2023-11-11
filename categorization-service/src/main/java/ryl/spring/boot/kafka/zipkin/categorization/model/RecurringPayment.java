package ryl.spring.boot.kafka.zipkin.categorization.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.Period;
import java.time.ZonedDateTime;

import static ryl.spring.boot.kafka.zipkin.categorization.configuration.ClockConfiguration.ISO_ZONED_DATE_TIME;
import static ryl.spring.boot.kafka.zipkin.categorization.model.CategoryType.RECURRING_PAYMENT;

final class RecurringPayment extends CategorizedTransaction {

    @JsonFormat(pattern = ISO_ZONED_DATE_TIME)
    private final ZonedDateTime initialChargeDate;
    private final BigDecimal amount;
    private final Period periodicity;

    RecurringPayment(Transaction transaction, ZonedDateTime now) {
        super(transaction);
        this.initialChargeDate = now;
        this.amount = BigDecimal.valueOf(100.0);
        this.periodicity = Period.ofMonths(1);
    }

    @Override
    public CategoryType calculateCategory() {
        return RECURRING_PAYMENT;
    }

    public ZonedDateTime getInitialChargeDate() {
        return initialChargeDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Period getPeriodicity() {
        return periodicity;
    }
}
