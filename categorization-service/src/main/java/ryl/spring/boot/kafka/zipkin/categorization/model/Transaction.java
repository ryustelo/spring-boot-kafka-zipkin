package ryl.spring.boot.kafka.zipkin.categorization.model;

public record Transaction(Long transactionId) {

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    public static class TransactionBuilder {

        private Long transactionId;

        public TransactionBuilder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Transaction build() {
            return new Transaction(this.transactionId);
        }
    }
}
