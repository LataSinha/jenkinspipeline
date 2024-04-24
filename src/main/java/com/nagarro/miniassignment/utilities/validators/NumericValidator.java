package com.nagarro.miniassignment.utilities.validators;

import com.nagarro.miniassignment.utilities.exceptions.ValidationException;

public class NumericValidator implements Validator<Integer> {

    private static NumericValidator instance;

    private NumericValidator() {
        // Private constructor to prevent instantiation
    }

    public static NumericValidator getInstance() {
        if (instance == null) {
            instance = new NumericValidator();
        }
        return instance;
    }

    @Override
    public void validate(Integer value) {
        String stringValue = String.valueOf(value);
        if (!stringValue.matches("\\d+")) {
            throw new ValidationException("Numeric validation failed for value: " + value);
        }
    }

}

