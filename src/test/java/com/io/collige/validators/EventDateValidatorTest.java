/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import com.io.collige.models.requests.calendar.CalendarEventsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventDateValidatorTest {

    @InjectMocks
    private EventDateValidator eventDateValidator;

    @Test
    void isValid_error() {
        CalendarEventsRequest request = new CalendarEventsRequest();
        request.setTimeMax(null);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventDateValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_before() {
        CalendarEventsRequest request = new CalendarEventsRequest();
        request.setTimeMin(LocalDateTime.MAX);
        request.setTimeMax(LocalDateTime.MIN);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = eventDateValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid() {
        CalendarEventsRequest request = new CalendarEventsRequest();
        request.setTimeMin(LocalDateTime.MIN);
        request.setTimeMax(LocalDateTime.MAX);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = eventDateValidator.isValid(request, context);
        assertTrue(response);
    }

}