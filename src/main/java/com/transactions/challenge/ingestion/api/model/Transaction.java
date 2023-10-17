package com.transactions.challenge.ingestion.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Transaction Ingestion Request")
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
public record Transaction(
        @Schema(description = "Transaction identifier", example = "123456789")
        @NotNull Long transactionId) {

    public Transaction(Long transactionId) {
        this.transactionId = transactionId;
    }

    public static TransactionBuilder builder() {
        return new TransactionBuilder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionBuilder {

        @NotEmpty
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
