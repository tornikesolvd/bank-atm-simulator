package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.Withdrawal;

import java.math.BigDecimal;
import java.util.Set;

public final class WithdrawalValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "GEL");

    private WithdrawalValidator() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    public static boolean isValid(Withdrawal withdrawal) {
        if (withdrawal == null) return false;
        return isValidAmount(withdrawal.getTotalAmount()) &&
               isValidCurrency(withdrawal.getCurrency()) &&
               isValidAccountId(withdrawal.getAccountId()) &&
               isValidTransactionId(withdrawal.getTransactionId()) &&
               isValidAtmId(withdrawal.getAtmId());
    }

    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) return false;
        return amount.compareTo(Withdrawal.MIN_AMOUNT) >= 0;
    }

    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) return false;
        return SUPPORTED_CURRENCIES.contains(currency.trim().toUpperCase());
    }

    public static boolean isValidAccountId(Long accountId) {
        return accountId != null && accountId > 0;
    }

    public static boolean isValidTransactionId(Long transactionId) {
        return transactionId != null && transactionId > 0;
    }

    public static boolean isValidAtmId(Long atmId) {
        return atmId != null && atmId > 0;
    }
}

