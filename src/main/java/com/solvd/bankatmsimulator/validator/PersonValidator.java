package com.solvd.bankatmsimulator.validator;

import com.solvd.bankatmsimulator.domain.entity.Person;

import java.util.regex.Pattern;

public final class PersonValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$"
    );

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;

    private PersonValidator() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    public static boolean isValid(Person person) {
        if (person == null) return false;
        return isValidFullName(person.getFullName()) &&
               isValidEmail(person.getEmail()) &&
               isValidPhoneNumber(person.getPhoneNumber());
    }

    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return false;
        String trimmed = fullName.trim();
        return trimmed.length() >= MIN_NAME_LENGTH &&
               trimmed.length() <= MAX_NAME_LENGTH &&
               trimmed.matches("^[a-zA-Z\\s'-]+$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) return false;
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }
}

