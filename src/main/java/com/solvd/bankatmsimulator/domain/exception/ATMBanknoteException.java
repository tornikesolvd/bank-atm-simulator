package com.solvd.bankatmsimulator.domain.exception;

public class ATMBanknoteException extends RuntimeException {

    public ATMBanknoteException(String message) {
        super(message);
    }

    public static ATMBanknoteException banknoteIsNull() {
        return new ATMBanknoteException("ATM banknote cannot be null.");
    }

    public static ATMBanknoteException invalidId() {
        return new ATMBanknoteException("Banknote ID cannot be null or negative.");
    }

    public static ATMBanknoteException invalidAtmId() {
        return new ATMBanknoteException("ATM ID cannot be null or negative.");
    }

    public static ATMBanknoteException invalidCurrency() {
        return new ATMBanknoteException("Currency cannot be null or empty.");
    }

    public static ATMBanknoteException invalidDenomination() {
        return new ATMBanknoteException("Denomination must be positive.");
    }

    public static ATMBanknoteException invalidQuantity() {
        return new ATMBanknoteException("Quantity cannot be null or negative.");
    }

    public static ATMBanknoteException notFound(Long id) {
        return new ATMBanknoteException("ATM banknote with id " + id + " not found.");
    }

    public static ATMBanknoteException emptyList() {
        return new ATMBanknoteException("No ATM banknotes found.");
    }
}
