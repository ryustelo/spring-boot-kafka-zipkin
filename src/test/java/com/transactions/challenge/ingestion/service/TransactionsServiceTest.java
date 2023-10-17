package com.transactions.challenge.ingestion.service;

import com.transactions.challenge.ingestion.api.model.Transaction;
import com.transactions.challenge.ingestion.event.publisher.TransactionsPublisher;
import com.transactions.challenge.ingestion.exception.TransactionPublisherException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class TransactionsServiceTest {

    @Mock
    private TransactionsPublisher transactionsPublisher;

    @InjectMocks
    private TransactionsService underTest;

    @Test
    void givenRequest_whenIngest_thenTransactionSuccessfullyIngested(CapturedOutput output) {
        Transaction request = Transaction.builder()
                .transactionId(111L).build();

        underTest.ingest(request);

        verify(transactionsPublisher).publish(request);
        assertThat(output).contains("Transaction successfully ingested");
    }

    @Test
    void givenPublisherThrowsException_whenIngest_thenTransactionNotIngested(CapturedOutput output) {
        Transaction request = Transaction.builder().transactionId(111L).build();
        doThrow(new TransactionPublisherException(new Throwable("Unable to send message")))
                .when(transactionsPublisher).publish(request);

        underTest.ingest(request);

        verify(transactionsPublisher).publish(request);
        assertThat(output).contains("Error while trying to ingest transaction");
    }
}