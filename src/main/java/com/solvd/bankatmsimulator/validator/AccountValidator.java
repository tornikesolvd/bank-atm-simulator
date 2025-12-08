package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.Account;

import java.math.BigDecimal;
import java.util.Set;

public final class AccountValidator {

    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "GEL");
    private static final int ACCOUNT_NUMBER_LENGTH = 16;
    private static final BigDecimal MIN_BALANCE = BigDecimal.ZERO;

    private AccountValidator() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    public static boolean isValid(Account account) {
        if (account == null) return false;
        return isValidAccountNumber(account.getAccountNumber()) &&
                isValidBalance(account.getBalance()) &&
                isValidCurrency(account.getCurrency());
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) return false;
        String trimmed = accountNumber.trim();
        return trimmed.length() == ACCOUNT_NUMBER_LENGTH &&
                trimmed.matches("^\\d+$");
    }

    public static boolean isValidBalance(BigDecimal balance) {
        if (balance == null) return false;
        return balance.compareTo(MIN_BALANCE) >= 0;
    }

    public static boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) return false;
        return SUPPORTED_CURRENCIES.contains(currency.trim().toUpperCase());
    }
}

