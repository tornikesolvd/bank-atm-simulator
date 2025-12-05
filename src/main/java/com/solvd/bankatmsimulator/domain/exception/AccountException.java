package com.solvd.bankatmsimulator.domain.exception;

public class AccountException extends RuntimeException {

    public AccountException(String message) {
        super(message);
    }

    public static AccountException accountIsNull() {
        return new AccountException("Account cannot be null.");
    }

    public static AccountException invalidId() {
        return new AccountException("Account ID cannot be null or negative.");
    }

    public static AccountException invalidNumber() {
        return new AccountException("Account number cannot be null or empty.");
    }

    public static AccountException invalidBalance() {
        return new AccountException("Balance cannot be null or negative.");
    }

    public static AccountException invalidCurrency() {
        return new AccountException("Currency cannot be null or empty.");
    }

    public static AccountException notFound(Long id) {
        return new AccountException("Account with id " + id + " not found.");
    }

    public static AccountException emptyList() {
        return new AccountException("No accounts found.");
    }

}
