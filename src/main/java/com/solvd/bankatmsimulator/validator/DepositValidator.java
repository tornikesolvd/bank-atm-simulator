package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.Deposit;

import java.math.BigDecimal;
import java.util.Set;

public final class DepositValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "GEL");

    private DepositValidator() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    public static boolean isValid(Deposit deposit) {
        if (deposit == null) return false;
        return isValidAmount(deposit.getTotalAmount()) &&
                isValidCurrency(deposit.getCurrency()) &&
                isValidTransactionId(deposit.getTransactionId()) &&
                isValidAtmId(deposit.getAtmId());
    }

    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(Deposit.MIN_AMOUNT) >= 0;
    }

    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) return false;
        return SUPPORTED_CURRENCIES.contains(currency.trim().toUpperCase());
    }

    public static boolean isValidTransactionId(Long transactionId) {
        return transactionId != null && transactionId > 0;
    }

    public static boolean isValidAtmId(Long atmId) {
        return atmId != null && atmId > 0;
    }
}

