package com.solvd.bankatmsimulator.domain.exception;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }

    public static TransactionException transactionIsNull() {
        return new TransactionException("Transaction cannot be null.");
    }

    public static TransactionException invalidId() {
        return new TransactionException("Transaction ID cannot be null or negative.");
    }

    public static TransactionException invalidAmount() {
        return new TransactionException("Amount must be positive.");
    }

    public static TransactionException invalidCurrency() {
        return new TransactionException("Currency cannot be null or empty.");
    }

    public static TransactionException invalidType() {
        return new TransactionException("Transaction type cannot be null.");
    }

    public static TransactionException invalidStatus() {
        return new TransactionException("Transaction status cannot be null.");
    }

    public static TransactionException invalidProcessedAt() {
        return new TransactionException("ProcessedAt cannot be null.");
    }

    public static TransactionException notFound(Long id) {
        return new TransactionException("Transaction with id " + id + " not found.");
    }

    public static TransactionException emptyList() {
        return new TransactionException("No transactions found.");
    }
}
