package ryl.spring.boot.kafka.zipkin.categorization.model;

import java.math.BigDecimal;

import static ryl.spring.boot.kafka.zipkin.categorization.model.CategoryType.REFUND;

final class Refund extends CategorizedTransaction {

    private final BigDecimal amount;
    private final String concept;

    Refund(Transaction transaction) {
        super(transaction);
        this.amount = BigDecimal.valueOf(450.0);
        this.concept = "Wrong product received";
    }

    @Override
    public CategoryType calculateCategory() {
        return REFUND;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getConcept() {
        return concept;
    }
}
