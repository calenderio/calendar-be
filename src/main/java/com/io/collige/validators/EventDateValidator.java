/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.validators;

import com.io.collige.models.requests.calendar.CalendarEventsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventDateValidator implements ConstraintValidator<EventDate, CalendarEventsRequest> {

    @Override
    public boolean isValid(CalendarEventsRequest field, ConstraintValidatorContext context) {
        if (field.getTimeMin() != null && field.getTimeMax() != null) {
            return field.getTimeMin().isBefore(field.getTimeMax()) || field.getTimeMax().equals(field.getTimeMin());
        } else if (field.getTimeMin() == null || field.getTimeMax() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{event.null.value}")
                    .addPropertyNode("TIME_NULL").addConstraintViolation();
            return false;
        }
        return true;
    }

}
