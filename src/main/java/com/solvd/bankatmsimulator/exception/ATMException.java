package com.solvd.bankatmsimulator.exception;

public class ATMException extends RuntimeException {

    public ATMException(String message) {
        super(message);
    }

    public static ATMException atmIsNull() {
        return new ATMException("ATM cannot be null.");
    }

    public static ATMException invalidId() {
        return new ATMException("ATM ID cannot be null or negative.");
    }

    public static ATMException invalidLocation() {
        return new ATMException("ATM location cannot be null or empty.");
    }

    public static ATMException invalidName() {
        return new ATMException("ATM name cannot be null or empty.");
    }

    public static ATMException notFound(Long id) {
        return new ATMException("ATM with id " + id + " not found.");
    }

    public static ATMException emptyList() {
        return new ATMException("No ATMs found.");
    }
}
