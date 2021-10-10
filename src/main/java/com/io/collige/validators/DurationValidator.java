/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.collige.validators;

import com.io.collige.enums.DurationType;
import com.io.collige.models.internals.event.CalendarDuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DurationValidator implements ConstraintValidator<Duration, CalendarDuration> {

    @Override
    public boolean isValid(CalendarDuration field, ConstraintValidatorContext context) {
        if (DurationType.HOUR.equals(field.getDurationType()) && (field.getDuration() > 24 || field.getDuration() < 1)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{duration.hour.error}")
                    .addPropertyNode("duration").addConstraintViolation();
            return false;
        } else if (DurationType.MIN.equals(field.getDurationType()) && (field.getDuration() > 60 || field.getDuration() < 1)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{duration.min.error}")
                    .addPropertyNode("duration").addConstraintViolation();
            return false;
        }
        return true;

    }

}
