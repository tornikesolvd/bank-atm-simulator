package com.solvd.bankatmsimulator.domain.exception;

public class PaymentCardException extends RuntimeException {

    public PaymentCardException(String message) {
        super(message);
    }

    public static PaymentCardException cardIsNull() {
        return new PaymentCardException("Payment card cannot be null.");
    }

    public static PaymentCardException invalidId() {
        return new PaymentCardException("Payment card ID cannot be null or negative.");
    }

    public static PaymentCardException invalidCardNumber() {
        return new PaymentCardException("Card number cannot be null or empty.");
    }

    public static PaymentCardException invalidCardType() {
        return new PaymentCardException("Card type cannot be null.");
    }

    public static PaymentCardException invalidStatus() {
        return new PaymentCardException("Card status cannot be null.");
    }

    public static PaymentCardException invalidPinHash() {
        return new PaymentCardException("PIN hash cannot be null or empty.");
    }

    public static PaymentCardException invalidExpiryDate() {
        return new PaymentCardException("Expiry date cannot be null or in the past.");
    }

    public static PaymentCardException notFound(Long id) {
        return new PaymentCardException("Payment card with id " + id + " not found.");
    }

    public static PaymentCardException emptyList() {
        return new PaymentCardException("No payment cards found.");
    }
}
