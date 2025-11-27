package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.PaymentCard;

import static com.solvd.bankatmsimulator.domain.PaymentCard.CardIssuer.*;

public final class PaymentCardValidator {

    private PaymentCardValidator() {
        throw new IllegalStateException("Validator class, do not instantiate");
    }

    public static boolean isValid(String cardNumber) {
        if (cardNumber == null) return false;

        String digits = cardNumber.replaceAll("[ -]", "");
        if (!digits.matches("\\d+"))
            return false;

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = digits.length() - 1; i >= 0; i--) {
            int digit = digits.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    public static String mask(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4)
            return "****";

        String digits = cardNumber.replaceAll("[ -]", "");
        if (digits.length() < 4)
            return "****";
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }

    public static PaymentCard.CardIssuer getCardType(String cardNumber) {
        if (cardNumber == null)
            return UNKNOWN;

        String digits = cardNumber.replaceAll("[ -]", "");

        if (digits.matches("^4\\d{12}(\\d{3})?$"))
            return VISA;
        if (digits.matches("^5[1-5]\\d{14}$"))
            return MASTERCARD;
        if (digits.matches("^3[47]\\d{13}$"))
            return AMERICAN_EXPRESS;
        if (digits.matches("^6(?:011|5\\d{2})\\d{12}$"))
            return DISCOVER;

        return UNKNOWN;
    }
}

