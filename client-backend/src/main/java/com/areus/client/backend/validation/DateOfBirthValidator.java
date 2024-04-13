package com.areus.client.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Validator for validating birthdates of format 'yyyy-mm-dd'
 */
public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, LocalDate> {

    /**
     * The method responsible for implementing the validation logic.
     * @param date The date to be validated.
     * @param context Provides contextual data and operation when applying a given constraint validator.
     * @return True if the date is valid
     */
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {

        /*
            Unfortunately it would be very difficult to write an exact regex for the date of birth.
            Thus, neither the following regex is perfect for checking birthdates.
            However, combined with the before() and after() methods of LocalDate, it provides quite a thorough check.
         */
        if (!date.toString().matches("^[0-9]{4}-[01]{1}[0-9]{1}-[0-3]{1}[0-9]{1}$")) {
            return false;
        } else if (date.isBefore(LocalDate.now().minusYears(120)) || date.isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }
}