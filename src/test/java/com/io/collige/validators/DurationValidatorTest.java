/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import com.io.collige.enums.DurationType;
import com.io.collige.models.internals.event.CalendarDuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DurationValidatorTest {

    @InjectMocks
    private DurationValidator durationValidator;

    @Test
    void isValid() {
        CalendarDuration duration = new CalendarDuration();
        duration.setDuration(1);
        duration.setDurationType(DurationType.MIN);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = durationValidator.isValid(duration, context);
        assertTrue(response);
    }

    @Test
    void isValid_minError() {
        CalendarDuration duration = new CalendarDuration();
        duration.setDuration(61);
        duration.setDurationType(DurationType.MIN);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = durationValidator.isValid(duration, context);
        assertFalse(response);
    }

    @Test
    void isValid_hourError() {
        CalendarDuration duration = new CalendarDuration();
        duration.setDuration(25);
        duration.setDurationType(DurationType.HOUR);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = durationValidator.isValid(duration, context);
        assertFalse(response);
    }
}