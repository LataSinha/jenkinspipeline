package com.nagarro.miniassignment.utilities.validators;

import com.nagarro.miniassignment.utilities.exceptions.ValidationException;

public class EnglishAlphabetValidator implements Validator<String> {

    private static EnglishAlphabetValidator instance;

    private EnglishAlphabetValidator() {
        // Private constructor to prevent instantiation
    }

    public static EnglishAlphabetValidator getInstance() {
        if (instance == null) {
            instance = new EnglishAlphabetValidator();
        }
        return instance;
    }

    @Override
    public void validate(String value) {
        if (!value.matches("[a-zA-Z]+")) {
            throw new ValidationException("English alphabets validation failed for value: " + value);
        }
    }
}
