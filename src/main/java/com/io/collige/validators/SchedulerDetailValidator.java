/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.validators;

import com.io.collige.constants.DayValues;
import com.io.collige.models.internals.event.AdditionalTime;
import com.io.collige.models.internals.scheduler.SchedulerDetails;
import com.io.collige.models.internals.scheduler.SchedulerTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SchedulerDetailValidator implements ConstraintValidator<SchedulerDetail, SchedulerDetails> {

    @Override
    public boolean isValid(SchedulerDetails field, ConstraintValidatorContext context) {
        if (checkHours(field.getMon(), context, DayValues.MONDAY)) {
            return false;
        }
        if (checkHours(field.getTue(), context, DayValues.TUESDAY)) {
            return false;
        }
        if (checkHours(field.getWed(), context, DayValues.WEDNESDAY)) {
            return false;
        }
        if (checkHours(field.getThu(), context, DayValues.THURSDAY)) {
            return false;
        }
        if (checkHours(field.getFri(), context, DayValues.FRIDAY)) {
            return false;
        }
        if (checkHours(field.getSat(), context, DayValues.SATURDAY)) {
            return false;
        }
        if (checkHours(field.getSun(), context, DayValues.SUNDAY)) {
            return false;
        }
        if (additionalTimeCheck(field, context)) return false;
        if (additionalDetailsChecker(field, context)) return false;
        return field.getMon() != null || field.getTue() != null || field.getWed() != null || field.getThu() != null
                || field.getFri() != null || field.getSat() != null || field.getSun() != null
                || (field.getAdditional() != null && !field.getAdditional().isEmpty());

    }

    private boolean additionalDetailsChecker(SchedulerDetails field, ConstraintValidatorContext context) {
        if (field.getAdditional() != null) {
            for (AdditionalTime time : field.getAdditional()) {
                if (field.getUnavailable() != null && field.getUnavailable().contains(time.getDate())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{scheduler.additional.unav}")
                            .addPropertyNode("unavailable").addConstraintViolation();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean additionalTimeCheck(SchedulerDetails field, ConstraintValidatorContext context) {
        if (field.getAdditional() != null && !field.getAdditional().isEmpty()) {
            List<AdditionalTime> sorted = field.getAdditional().stream().sorted(Comparator.comparing(AdditionalTime::getDate))
                    .collect(Collectors.toList());
            if (checkHours(sorted.get(sorted.size() - 1).getTime(), context, DayValues.ADDITIONAL)) {
                return true;
            }
            if (hourChecker(context, sorted)) return true;
            for (AdditionalTime time : field.getAdditional()) {
                if (checkHours(time.getTime(), context, DayValues.ADDITIONAL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hourChecker(ConstraintValidatorContext context, List<AdditionalTime> sorted) {
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (checkHours(sorted.get(i).getTime(), context, DayValues.ADDITIONAL)) {
                return true;
            }
            if (sorted.get(i).getDate().equals(sorted.get(i + 1).getDate())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{scheduler.additional.duplicate}")
                        .addPropertyNode(DayValues.ADDITIONAL).addConstraintViolation();
                return true;
            }
        }
        return false;
    }

    private boolean checkHours(Set<SchedulerTime> set, ConstraintValidatorContext context, String fieldName) {
        if (set == null) {
            return false;
        }
        boolean isError1 = false;
        List<SchedulerTime> sorted = set.stream().sorted(Comparator.comparing(SchedulerTime::getStart))
                .collect(Collectors.toList());
        if (set.size() == 1) {
            isError1 = checkStartEnd(sorted.get(0));
        }
        int i = 0;
        boolean isError2 = false;
        boolean isError3 = false;
        while (!isError1 && !isError2 && !isError3 && i < sorted.size() - 1) {
            SchedulerTime time = sorted.get(i);
            SchedulerTime time2 = sorted.get(++i);
            isError1 = checkStartEnd(time);
            isError2 = checkStartEnd(time2);
            isError3 = timeCompare(time, time2);
        }
        if (isError1 || isError2) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{scheduler.date.start}")
                    .addPropertyNode(fieldName).addConstraintViolation();
            return true;
        }
        if (isError3) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{scheduler.date.violation}")
                    .addPropertyNode(fieldName).addConstraintViolation();
            return true;
        }
        return false;
    }

    private boolean checkStartEnd(SchedulerTime time) {
        String[] startSplit = time.getStart().split(":");
        String[] endSplit = time.getEnd().split(":");
        return !LocalDate.MIN.atTime(Integer.parseInt(startSplit[0]), Integer.parseInt(startSplit[1]))
                .isBefore(LocalDate.MIN.atTime(Integer.parseInt(endSplit[0]), Integer.parseInt(endSplit[1])));
    }

    private boolean timeCompare(SchedulerTime time, SchedulerTime time2) {
        String[] time1Split = time.getEnd().split(":");
        String[] time2Split = time2.getStart().split(":");
        return LocalDate.MIN.atTime(Integer.parseInt(time1Split[0]), Integer.parseInt(time1Split[1]))
                .isAfter(LocalDate.MIN.atTime(Integer.parseInt(time2Split[0]), Integer.parseInt(time2Split[1])));
    }

}
