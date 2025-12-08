package com.solvd.bankatmsimulator.exception;

public class DepositException extends RuntimeException {

    public DepositException(String message) {
        super(message);
    }

    public static DepositException depositIsNull() {
        return new DepositException("Deposit cannot be null.");
    }

    public static DepositException invalidId() {
        return new DepositException("Deposit ID cannot be null or negative.");
    }

    public static DepositException invalidTransactionId() {
        return new DepositException("Transaction ID cannot be null or negative.");
    }

    public static DepositException invalidAtmId() {
        return new DepositException("ATM ID cannot be null or negative.");
    }

    public static DepositException invalidCurrency() {
        return new DepositException("Currency cannot be null or empty.");
    }

    public static DepositException invalidAmount() {
        return new DepositException("Total amount must be greater than or equal to 1.00.");
    }

    public static DepositException invalidProcessedAt() {
        return new DepositException("ProcessedAt date cannot be null.");
    }

    public static DepositException notFound(Long id) {
        return new DepositException("Deposit with id " + id + " not found.");
    }

    public static DepositException emptyList() {
        return new DepositException("No deposits found.");
    }
}
