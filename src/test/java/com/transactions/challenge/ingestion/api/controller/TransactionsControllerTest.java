package com.transactions.challenge.ingestion.api.controller;

import com.transactions.challenge.ingestion.api.model.Transaction;
import com.transactions.challenge.ingestion.exception.TransactionPublisherException;
import com.transactions.challenge.ingestion.service.TransactionsService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionsControllerTest {

    @Value("${local.server.port}")
    private int port;

    private static final String TRANSACTIONS_INGESTION_URL = "/ingestion-service/transactions";

    @MockBean
    TransactionsService transactionsService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void givenValidRequest_whenPost_returnsAccepted() {
        Transaction request = Transaction.builder().transactionId(123L).build();
        doNothing().when(transactionsService).ingest(any(Transaction.class));

        assertTransactionsIngestionResponse(request, ACCEPTED);
    }

    @Test
    void givenInvalidRequest_whenPost_returnsBadRequest() {
        Transaction request = Transaction.builder().build();

        assertTransactionsIngestionResponse(request, BAD_REQUEST);
    }

    @Test
    void givenValidRequest_whenPost_returnsInternalServerError() {
        Transaction request = Transaction.builder().transactionId(123L).build();
        doThrow(new TransactionPublisherException()).when(transactionsService).ingest(request);

        assertTransactionsIngestionResponse(request, INTERNAL_SERVER_ERROR);
    }

    private static void assertTransactionsIngestionResponse(Transaction request, HttpStatus httpStatus) {
        with().contentType(JSON)
                .body(request)
                .when().post(TRANSACTIONS_INGESTION_URL)
                .then()
                .assertThat().statusCode(httpStatus.value());
    }
}