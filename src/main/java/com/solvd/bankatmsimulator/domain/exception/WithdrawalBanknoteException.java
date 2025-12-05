package com.solvd.bankatmsimulator.domain.exception;

public class WithdrawalBanknoteException extends RuntimeException {

    public WithdrawalBanknoteException(String message) {
        super(message);
    }

    public static WithdrawalBanknoteException entityIsNull() {
        return new WithdrawalBanknoteException("WithdrawalBanknote cannot be null.");
    }

    public static WithdrawalBanknoteException invalidId() {
        return new WithdrawalBanknoteException("WithdrawalBanknote ID is invalid.");
    }

    public static WithdrawalBanknoteException invalidWithdrawalId() {
        return new WithdrawalBanknoteException("Withdrawal ID is invalid.");
    }

    public static WithdrawalBanknoteException invalidDenomination() {
        return new WithdrawalBanknoteException("Denomination must be positive.");
    }

    public static WithdrawalBanknoteException invalidQuantity() {
        return new WithdrawalBanknoteException("Quantity must be positive.");
    }

    public static WithdrawalBanknoteException notFound(Long id) {
        return new WithdrawalBanknoteException("WithdrawalBanknote with id " + id + " not found.");
    }

    public static WithdrawalBanknoteException emptyList() {
        return new WithdrawalBanknoteException("No withdrawal banknotes found.");
    }
}
