/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.validators;

import com.io.fastmeet.models.internals.AdditionalTime;
import com.io.fastmeet.models.internals.SchedulerDetails;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SchedulerDetailValidator implements ConstraintValidator<SchedulerDetail, SchedulerDetails> {

    @Override
    public boolean isValid(SchedulerDetails field, ConstraintValidatorContext context) {
        for (AdditionalTime time : field.getAdditional()) {
            if (field.getUnavailable().contains(time.getDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{scheduler.additional.unav}")
                        .addPropertyNode("unavailable").addConstraintViolation();
                return false;
            }
        }
        return field.getMon() != null || field.getTue() != null || field.getWed() != null || field.getThu() != null
                || field.getFri() != null || field.getSat() != null || field.getSun() != null
                || (field.getAdditional() != null && !field.getAdditional().isEmpty());

    }

}
