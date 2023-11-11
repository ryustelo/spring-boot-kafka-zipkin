package ryl.spring.boot.kafka.zipkin.categorization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import ryl.spring.boot.kafka.zipkin.categorization.event.publisher.TransactionsPublisher;
import ryl.spring.boot.kafka.zipkin.categorization.exception.TransactionPublisherException;
import ryl.spring.boot.kafka.zipkin.categorization.model.Categorizable;
import ryl.spring.boot.kafka.zipkin.categorization.model.Transaction;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class TransactionsServiceTest {

    @Mock
    private TransactionsPublisher transactionsPublisher;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private TransactionsService underTest;

    private static final Instant INSTANT = Instant.parse("2023-10-22T23:51:00Z");

    @BeforeEach
    void setUp() {
        Mockito.when(clock.instant()).thenReturn(INSTANT);
    }

    @Test
    void givenTransaction_whenCategorize_thenSuccessfullyPublished(CapturedOutput output) {
        Transaction transaction = Transaction.builder().transactionId(1L).build();

        underTest.categorize(transaction);

        verify(transactionsPublisher).publish(any(Categorizable.class));
        assertThat(output).contains("Categorized transaction successfully published");
    }

    @Test
    void givenExceptionThrownOnPublish_whenCategorize_thenErrorLogged(CapturedOutput output) {
        Transaction transaction = Transaction.builder().transactionId(1L).build();
        doThrow(new TransactionPublisherException(new Exception()))
                .when(transactionsPublisher).publish(any(Categorizable.class));

        underTest.categorize(transaction);

        verify(transactionsPublisher).publish(any(Categorizable.class));
        assertThat(output).contains("Error while trying to ingest categorized transaction id");
    }
}