package ryl.spring.boot.kafka.zipkin.ingestion.exception;

public class TransactionPublisherException extends RuntimeException {

    public TransactionPublisherException() {
    }

    public TransactionPublisherException(Throwable cause) {
        super(cause);
    }
}
