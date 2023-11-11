package ryl.spring.boot.kafka.zipkin.categorization.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Period;
import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static ryl.spring.boot.kafka.zipkin.categorization.model.CategorizedTransactionFactory.create;
import static ryl.spring.boot.kafka.zipkin.categorization.model.CategoryType.*;

class CategorizedTransactionFactoryTest {

    private static final long ONE_OFF_PAYMENT_TRANSACTION_ID = 0L;
    private static final long RECURRING_PAYMENT_TRANSACTION_ID = 1L;
    private static final long REFUND_TRANSACTION_ID = 2L;

    @Test
    void givenOneOffPaymentTransaction_whenCreate_thenTransactionCorrectlyCategorized() {
        Transaction transaction = Transaction.builder().transactionId(ONE_OFF_PAYMENT_TRANSACTION_ID).build();
        ZonedDateTime now = now();

        OneOffPayment oneOffPayment = (OneOffPayment) create(transaction, now);

        assertThat(oneOffPayment.getCategory()).isEqualTo(ONE_OFF_PAYMENT);
        assertThat(oneOffPayment.getTransactionId()).isEqualTo(ONE_OFF_PAYMENT_TRANSACTION_ID);
        assertThat(oneOffPayment.getAmount()).isEqualTo(BigDecimal.valueOf(30.0));
        assertThat(oneOffPayment.getChargeDate()).isEqualTo(now);
    }

    @Test
    void givenRecurringPaymentTransaction_whenCreate_thenTransactionCorrectlyCategorized() {
        Transaction transaction = Transaction.builder().transactionId(RECURRING_PAYMENT_TRANSACTION_ID).build();
        ZonedDateTime now = now();

        RecurringPayment recurringPayment = (RecurringPayment) create(transaction, now);

        assertThat(recurringPayment.getCategory()).isEqualTo(RECURRING_PAYMENT);
        assertThat(recurringPayment.getTransactionId()).isEqualTo(RECURRING_PAYMENT_TRANSACTION_ID);
        assertThat(recurringPayment.getAmount()).isEqualTo(BigDecimal.valueOf(100.0));
        assertThat(recurringPayment.getInitialChargeDate()).isEqualTo(now);
        assertThat(recurringPayment.getPeriodicity()).isEqualTo(Period.ofMonths(1));
    }

    @Test
    void givenRefundTransaction_whenCreate_thenTransactionCorrectlyCategorized() {
        Transaction transaction = Transaction.builder().transactionId(REFUND_TRANSACTION_ID).build();
        ZonedDateTime now = now();

        Refund refund = (Refund) create(transaction, now);

        assertThat(refund.getCategory()).isEqualTo(REFUND);
        assertThat(refund.getTransactionId()).isEqualTo(REFUND_TRANSACTION_ID);
        assertThat(refund.getAmount()).isEqualTo(BigDecimal.valueOf(450.0));
        assertThat(refund.getConcept()).isEqualTo("Wrong product received");
    }
}