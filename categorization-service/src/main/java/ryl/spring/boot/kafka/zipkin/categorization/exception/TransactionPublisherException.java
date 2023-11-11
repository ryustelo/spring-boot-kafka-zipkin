package ryl.spring.boot.kafka.zipkin.categorization.exception;

public class TransactionPublisherException extends RuntimeException {

    public TransactionPublisherException(Throwable cause) {
        super(cause);
    }
}
