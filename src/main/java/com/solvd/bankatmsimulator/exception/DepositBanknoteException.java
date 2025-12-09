package com.solvd.bankatmsimulator.exception;

public class DepositBanknoteException extends RuntimeException {

    public DepositBanknoteException(String message) {
        super(message);
    }

    public static DepositBanknoteException banknoteIsNull() {
        return new DepositBanknoteException("Deposit banknote cannot be null.");
    }

    public static DepositBanknoteException invalidId() {
        return new DepositBanknoteException("Banknote ID cannot be null or negative.");
    }

    public static DepositBanknoteException invalidDepositId() {
        return new DepositBanknoteException("Deposit ID cannot be null or negative.");
    }

    public static DepositBanknoteException invalidDenomination() {
        return new DepositBanknoteException("Denomination must be greater than 0.");
    }

    public static DepositBanknoteException invalidQuantity() {
        return new DepositBanknoteException("Quantity cannot be null or negative.");
    }

    public static DepositBanknoteException notFound(Long id) {
        return new DepositBanknoteException("Deposit banknote with id " + id + " not found.");
    }

    public static DepositBanknoteException emptyList() {
        return new DepositBanknoteException("No deposit banknotes found.");
    }
}
