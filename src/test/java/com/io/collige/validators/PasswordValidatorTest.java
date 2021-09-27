/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @InjectMocks
    private PasswordValidator passwordValidator;

    @Test
    void isValid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = passwordValidator.isValid("ab@.134asdkl2A", context);
        assertTrue(response);
    }

    @Test
    void isValid_false() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = passwordValidator.isValid("試し12312312313", context);
        assertFalse(response);
    }

    @Test
    void isValid_Ascifalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = passwordValidator.isValid("試し試し12312312313", context);
        assertTrue(response);
    }

    @Test
    void isValid_falseLatin() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = passwordValidator.isValid("12312313", context);
        assertFalse(response);
    }

}