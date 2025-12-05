package com.solvd.bankatmsimulator.domain.exception;

public class WithdrawalException extends RuntimeException {

    public WithdrawalException(String message) {
        super(message);
    }

    public static WithdrawalException entityIsNull() {
        return new WithdrawalException("Withdrawal cannot be null.");
    }

    public static WithdrawalException invalidId() {
        return new WithdrawalException("Withdrawal ID cannot be null or negative.");
    }

    public static WithdrawalException invalidAccountId() {
        return new WithdrawalException("Account ID cannot be null or negative.");
    }

    public static WithdrawalException invalidTransactionId() {
        return new WithdrawalException("Transaction ID cannot be null or negative.");
    }

    public static WithdrawalException invalidAtmId() {
        return new WithdrawalException("ATM ID cannot be null or negative.");
    }

    public static WithdrawalException invalidAmount() {
        return new WithdrawalException("Withdrawal amount must be positive and within allowed limits.");
    }

    public static WithdrawalException invalidCurrency() {
        return new WithdrawalException("Currency cannot be null or empty.");
    }

    public static WithdrawalException invalidProcessedAt() {
        return new WithdrawalException("ProcessedAt cannot be null.");
    }

    public static WithdrawalException notFound(Long id) {
        return new WithdrawalException("Withdrawal with id " + id + " not found.");
    }

    public static WithdrawalException emptyList() {
        return new WithdrawalException("No withdrawals found.");
    }
}
