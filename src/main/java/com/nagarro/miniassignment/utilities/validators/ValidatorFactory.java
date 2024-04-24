package com.nagarro.miniassignment.utilities.validators;

public class ValidatorFactory {

    private ValidatorFactory() {
        // Private constructor to prevent instantiation
    }

    public static Validator<?> getValidator(Object parameter) {
        if (parameter instanceof String) {
            return EnglishAlphabetValidator.getInstance();
        } else {
            return NumericValidator.getInstance();
        }
    }
}
