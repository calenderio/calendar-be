/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.calendar.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*[^A-Za-z0-9]).{8,}$";
    private static final String REGEX_AZ = ".*[a-zA-Z].*";

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        if (field.matches("[0-9]+") || field.replaceAll("\\d", "").length() < 3) {
            return false;
        }
        boolean isAscii = field.matches(REGEX_AZ);
        if (!isAscii) {
            return true;
        }
        return field.matches(REGEX);
    }

}
