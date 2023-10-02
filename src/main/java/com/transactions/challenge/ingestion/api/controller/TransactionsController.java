package com.transactions.challenge.ingestion.api.controller;

import com.transactions.challenge.ingestion.api.model.TransactionRequest;
import com.transactions.challenge.ingestion.service.TransactionsService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@Tag(name = "ingestion-service", description = "Transactions Ingestion Controller")
@RequestMapping(path = "/ingestion-service",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class TransactionsController {

    private final TransactionsService transactionsService;

    @Autowired
    TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Transaction successfully ingested"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal error during ingestion")
    })
    @PostMapping("/transactions")
    public ResponseEntity<Void> ingestTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {

        transactionsService.ingest(transactionRequest);
        return ResponseEntity.status(ACCEPTED.value()).build();
    }
}
