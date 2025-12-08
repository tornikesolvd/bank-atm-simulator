package com.solvd.bankatmsimulator.exception;

public class PersonException extends RuntimeException {

    public PersonException(String message) {
        super(message);
    }

    public static PersonException personIsNull() {
        return new PersonException("Person cannot be null.");
    }

    public static PersonException invalidId() {
        return new PersonException("Person ID cannot be null or negative.");
    }

    public static PersonException invalidFullName() {
        return new PersonException("Full name cannot be null or empty.");
    }

    public static PersonException invalidEmail() {
        return new PersonException("Email cannot be null or empty.");
    }

    public static PersonException invalidPhone() {
        return new PersonException("Phone number cannot be null or empty.");
    }

    public static PersonException notFound(Long id) {
        return new PersonException("Person with id " + id + " not found.");
    }

    public static PersonException emptyList() {
        return new PersonException("No persons found.");
    }
}
