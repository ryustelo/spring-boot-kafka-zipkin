package com.transactions.challenge.ingestion.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Transaction Ingestion Request")
@JsonDeserialize(builder = TransactionRequest.TransactionRequestBuilder.class)
public record TransactionRequest(
        @Schema(description = "Transaction identifier", example = "123456789")
        @NotNull Long transactionId) {

    public TransactionRequest(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "{\"transactionId\": %s}".formatted(this.transactionId);
    }

    public static TransactionRequestBuilder builder() {
        return new TransactionRequestBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionRequestBuilder {

        @NotEmpty
        private Long transactionId;

        public TransactionRequest.TransactionRequestBuilder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public TransactionRequest build() {
            return new TransactionRequest(this.transactionId);
        }
    }
}
