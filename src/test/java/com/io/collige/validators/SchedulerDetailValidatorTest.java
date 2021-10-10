/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import com.io.collige.models.internals.event.AdditionalTime;
import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.models.internals.scheduler.SchedulerTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchedulerDetailValidatorTest {

    @InjectMocks
    private SchedulerDetailValidator schedulerDetailValidator;

    @Test
    void isValid_timenotvalid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        details.setMon(Collections.singleton(new SchedulerTime("12:00", "09:00")));
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_MultiTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setMon(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_tueTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setTue(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_wedTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setWed(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_thuTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setThu(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_friTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setFri(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_sunTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setSun(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_satTimeError() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("09:00", "11:00"));
        details.setSat(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_timeTrue() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        details.setSat(schedulerTimeSet);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertTrue(response);
    }

    @Test
    void isValid_additionalTrue() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        details.setSat(schedulerTimeSet);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertTrue(response);
    }

    @Test
    void isValid_additionalFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("12:00", "09:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        details.setSat(schedulerTimeSet);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_unavaFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        details.setSat(schedulerTimeSet);
        details.setAdditional(additionalTimes);
        details.setUnavailable(Collections.singleton(LocalDate.now()));
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_timeWrongFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("12:00", "09:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        Set<SchedulerTime> schedulerTimeSet2 = new HashSet<>();
        schedulerTimeSet2.add(new SchedulerTime("09:00", "12:00"));
        details.setSat(schedulerTimeSet2);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

    @Test
    void isValid_timeWrongMultiTrue() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        schedulerTimeSet.add(new SchedulerTime("12:00", "13:00"));
        schedulerTimeSet.add(new SchedulerTime("13:00", "14:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        Set<SchedulerTime> schedulerTimeSet2 = new HashSet<>();
        schedulerTimeSet2.add(new SchedulerTime("09:00", "12:00"));
        details.setSat(schedulerTimeSet2);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertTrue(response);
    }

    @Test
    void isValid_timeWrongMultiFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        AdditionalTime additionalTime2 = new AdditionalTime();
        additionalTime2.setTime(Collections.singleton(new SchedulerTime("09:00", "11:00")));
        additionalTime2.setDate(LocalDate.now().plusDays(1));
        additionalTimes.add(additionalTime);
        additionalTimes.add(additionalTime2);
        Set<SchedulerTime> schedulerTimeSet2 = new HashSet<>();
        schedulerTimeSet2.add(new SchedulerTime("09:00", "12:00"));
        details.setSat(schedulerTimeSet2);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertTrue(response);
    }

    @Test
    void isValid_timeWrongMultiFalseAll() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        SchedulerDetails details = new SchedulerDetails();
        Set<SchedulerTime> schedulerTimeSet = new HashSet<>();
        schedulerTimeSet.add(new SchedulerTime("09:00", "12:00"));
        Set<AdditionalTime> additionalTimes = new HashSet<>();
        AdditionalTime additionalTime = new AdditionalTime();
        additionalTime.setTime(schedulerTimeSet);
        additionalTime.setDate(LocalDate.now());
        AdditionalTime additionalTime2 = new AdditionalTime();
        additionalTime2.setTime(Collections.singleton(new SchedulerTime("09:00", "11:00")));
        additionalTime2.setDate(LocalDate.now());
        additionalTimes.add(additionalTime);
        additionalTimes.add(additionalTime2);
        Set<SchedulerTime> schedulerTimeSet2 = new HashSet<>();
        schedulerTimeSet2.add(new SchedulerTime("09:00", "12:00"));
        details.setSat(schedulerTimeSet2);
        details.setAdditional(additionalTimes);
        boolean response = schedulerDetailValidator.isValid(details, context);
        assertFalse(response);
    }

}