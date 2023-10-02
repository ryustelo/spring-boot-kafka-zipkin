package com.transactions.challenge.ingestion.service;

import com.transactions.challenge.ingestion.api.model.TransactionRequest;
import com.transactions.challenge.ingestion.exception.TransactionPublisherException;
import com.transactions.challenge.ingestion.repository.TransactionsPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void givenRequest_whenIngest_thenTransactionSuccessfullyIngested(CapturedOutput output) throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .transactionId(111L).build();

        underTest.ingest(request);

        verify(transactionsPublisher).publishMessage(request);
        assertThat(output).contains("Transaction successfully ingested");
    }

    @Test
    void givenPublisherThrowsException_whenIngest_thenTransactionNotIngested(CapturedOutput output) throws Exception {
        TransactionRequest request = TransactionRequest.builder().transactionId(111L).build();
        doThrow(new InterruptedException())
                .when(transactionsPublisher).publishMessage(request);

        assertThrows(TransactionPublisherException.class, () -> underTest.ingest(request));

        verify(transactionsPublisher).publishMessage(request);
        assertThat(output).contains("Error while trying to ingest transaction");
    }
}