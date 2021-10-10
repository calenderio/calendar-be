/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import com.io.collige.constants.CacheConstants;
import com.io.collige.constants.RoleConstants;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.core.services.CacheService;
import com.io.collige.models.internals.event.QuestionModel;
import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.models.requests.events.EventCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventValidatorTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private EventValidator eventValidator;

    @Test
    void isValid() {
        EventCreateRequest request = new EventCreateRequest();
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_allSchedule() {
        EventCreateRequest request = new EventCreateRequest();
        request.setSchedule(new SchedulerDetails());
        request.setPreDefinedSchedulerId(1L);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_fileEmpty() {
        EventCreateRequest request = new EventCreateRequest();
        request.setPreDefinedSchedulerId(1L);
        request.setIsFileRequired(true);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_questionError() {
        EventCreateRequest request = new EventCreateRequest();
        request.setPreDefinedSchedulerId(1L);
        request.setQuestions(Collections.singletonList(new QuestionModel()));
        when(jwtService.getUserRoles()).thenReturn(Collections.singleton(RoleConstants.COMMERCIAL));
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)).thenReturn(0);
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_COMMERCIAL)).thenReturn(0);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_questionIndiError() {
        EventCreateRequest request = new EventCreateRequest();
        request.setPreDefinedSchedulerId(1L);
        request.setQuestions(Collections.singletonList(new QuestionModel()));
        when(jwtService.getUserRoles()).thenReturn(Collections.singleton(RoleConstants.INDIVIDUAL));
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)).thenReturn(0);
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_IND)).thenReturn(0);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_questionFreeError() {
        EventCreateRequest request = new EventCreateRequest();
        request.setPreDefinedSchedulerId(1L);
        request.setQuestions(Collections.singletonList(new QuestionModel()));
        when(jwtService.getUserRoles()).thenReturn(Collections.singleton(RoleConstants.FREE));
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)).thenReturn(0);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        boolean response = eventValidator.isValid(request, context);
        assertFalse(response);
    }

    @Test
    void isValid_true() {
        EventCreateRequest request = new EventCreateRequest();
        request.setPreDefinedSchedulerId(1L);
        request.setQuestions(Collections.singletonList(new QuestionModel()));
        when(jwtService.getUserRoles()).thenReturn(Collections.singleton(RoleConstants.INDIVIDUAL));
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)).thenReturn(0);
        when(cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_IND)).thenReturn(2);
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        boolean response = eventValidator.isValid(request, context);
        assertTrue(response);
    }


}