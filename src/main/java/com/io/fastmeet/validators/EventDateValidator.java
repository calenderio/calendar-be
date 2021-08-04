/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.validators;

import com.io.fastmeet.models.requests.calendar.CalendarEventsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventDateValidator implements ConstraintValidator<EventDate, CalendarEventsRequest> {

    @Override
    public boolean isValid(CalendarEventsRequest field, ConstraintValidatorContext context) {
        if (field.getTimeMin() != null && field.getTimeMax() != null) {
            return field.getTimeMin().isBefore(field.getTimeMax()) || field.getTimeMax().equals(field.getTimeMin());
        }
        return true;
    }

}
