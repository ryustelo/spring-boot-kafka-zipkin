package ryl.spring.boot.kafka.zipkin.categorization.model;

import java.time.ZonedDateTime;

public class CategorizedTransactionFactory {

    public static Categorizable create(Transaction transaction, ZonedDateTime now) {
        return switch (calculateCategory(transaction)) {
            case ONE_OFF_PAYMENT -> new OneOffPayment(transaction, now);
            case RECURRING_PAYMENT -> new RecurringPayment(transaction, now);
            case REFUND -> new Refund(transaction);
        };
    }

    private static CategoryType calculateCategory(Transaction transaction) {
        return CategoryType.valueByModulo(
                transaction.transactionId().intValue() % CategoryType.values().length);
    }
}
