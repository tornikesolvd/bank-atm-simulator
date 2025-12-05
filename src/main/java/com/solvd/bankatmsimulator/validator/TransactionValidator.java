package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.entity.Transaction;

import java.math.BigDecimal;
import java.util.Set;

public final class TransactionValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "GEL");
    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");

    private TransactionValidator() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    public static boolean isValid(Transaction transaction) {
        if (transaction == null) return false;
        return isValidAmount(transaction.getAmount()) &&
               isValidCurrency(transaction.getCurrency()) &&
               isValidTransactionType(transaction.getTransactionType()) &&
               isValidStatus(transaction.getStatus()) &&
               isValidAccountIds(transaction);
    }

    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(MIN_AMOUNT) >= 0;
    }

    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) return false;
        return SUPPORTED_CURRENCIES.contains(currency.trim().toUpperCase());
    }

    public static boolean isValidTransactionType(Transaction.TransactionType type) {
        return type != null;
    }

    public static boolean isValidStatus(Transaction.TransactionStatus status) {
        return status != null;
    }

    public static boolean isValidAccountIds(Transaction transaction) {
        if (transaction == null) return false;
        
        Transaction.TransactionType type = transaction.getTransactionType();
        if (type == null) return false;

        return switch (type) {
            case DEPOSIT -> transaction.getToAccountId() != null && transaction.getFromAccountId() == null;
            case WITHDRAWAL -> transaction.getFromAccountId() != null && transaction.getToAccountId() == null;
            case TRANSFER -> transaction.getFromAccountId() != null && 
                           transaction.getToAccountId() != null &&
                           !transaction.getFromAccountId().equals(transaction.getToAccountId());
        };
    }
}

