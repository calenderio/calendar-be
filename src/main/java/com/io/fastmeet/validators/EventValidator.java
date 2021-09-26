/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.fastmeet.validators;

import com.io.fastmeet.constants.CacheConstants;
import com.io.fastmeet.constants.RoleConstants;
import com.io.fastmeet.core.security.jwt.JWTService;
import com.io.fastmeet.core.services.CacheService;
import com.io.fastmeet.models.requests.calendar.EventTypeCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class EventValidator implements ConstraintValidator<Event, EventTypeCreateRequest> {

    private static final String EVENT_QUESTION_OVERSIZE = "{event.question.oversize}";
    private static final String QUESTIONS = "questions";

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean isValid(EventTypeCreateRequest field, ConstraintValidatorContext context) {
        return checkScheduler(field, context) && checkRolePermission(field, context) && checkFile(field, context);

    }

    private boolean overSize(ConstraintValidatorContext context, String s, String questions) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(s)
                .addPropertyNode(questions).addConstraintViolation();
        return false;
    }

    private boolean checkScheduler(EventTypeCreateRequest field, ConstraintValidatorContext context) {
        if (field.getPreDefinedSchedulerId() == null && field.getSchedule() == null) {
            return overSize(context, "{event.schedule.null}", "schedule");
        }
        if (field.getPreDefinedSchedulerId() != null && field.getSchedule() != null) {
            return overSize(context, "{event.schedule.notnull}", "schedule");
        }
        return true;
    }

    private boolean checkFile(EventTypeCreateRequest field, ConstraintValidatorContext context) {
        if (Boolean.TRUE.equals(field.getIsFileRequired()) && StringUtils.isBlank(field.getFileDescription())) {
            return overSize(context, "{event.filedescription.null}", "fileDescription");
        }
        return true;
    }

    private boolean checkRolePermission(EventTypeCreateRequest field, ConstraintValidatorContext context) {
        if (field.getQuestions() != null) {
            if (field.getQuestions().size() > cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)) {
                Set<String> roles = jwtService.getUserRoles();
                if (roles.contains(RoleConstants.COMMERCIAL)) {
                    if (field.getQuestions().size() > cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_COMMERCIAL)) {
                        return overSize(context, EVENT_QUESTION_OVERSIZE, QUESTIONS);
                    }
                    return true;
                } else if (roles.contains(RoleConstants.INDIVIDUAL)) {
                    if (field.getQuestions().size() > cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_IND)) {
                        return overSize(context, EVENT_QUESTION_OVERSIZE, QUESTIONS);
                    }
                    return true;
                } else {
                    return overSize(context, EVENT_QUESTION_OVERSIZE, QUESTIONS);
                }
            }
        }
        return true;
    }

}
