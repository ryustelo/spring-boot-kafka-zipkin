package ryl.spring.boot.kafka.zipkin.categorization.model;

public abstract sealed class CategorizedTransaction implements Categorizable
        permits Refund, RecurringPayment, OneOffPayment {

    protected final Long transactionId;
    private final CategoryType category;

    CategorizedTransaction(Transaction transaction) {
        this.transactionId = transaction.transactionId();
        this.category = calculateCategory();
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public CategoryType getCategory() {
        return category;
    }
}
