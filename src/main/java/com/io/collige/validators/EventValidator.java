/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.validators;

import com.io.collige.constants.CacheConstants;
import com.io.collige.constants.RoleConstants;
import com.io.collige.core.security.jwt.JWTService;
import com.io.collige.core.services.CacheService;
import com.io.collige.models.requests.events.EventCreateRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class EventValidator implements ConstraintValidator<Event, EventCreateRequest> {

    private static final String EVENT_QUESTION_OVERSIZE = "{event.question.oversize}";
    private static final String QUESTIONS = "questions";

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CacheService cacheService;

    @Override
    public boolean isValid(EventCreateRequest field, ConstraintValidatorContext context) {
        return checkScheduler(field, context) && checkRolePermission(field, context) && checkFile(field, context);

    }

    private boolean overSize(ConstraintValidatorContext context, String s, String questions) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(s)
                .addPropertyNode(questions).addConstraintViolation();
        return false;
    }

    private boolean checkScheduler(EventCreateRequest field, ConstraintValidatorContext context) {
        if (field.getPreDefinedSchedulerId() == null && field.getSchedule() == null) {
            return overSize(context, "{event.schedule.null}", "schedule");
        }
        if (field.getPreDefinedSchedulerId() != null && field.getSchedule() != null) {
            return overSize(context, "{event.schedule.notnull}", "schedule");
        }
        return true;
    }

    private boolean checkFile(EventCreateRequest field, ConstraintValidatorContext context) {
        if (Boolean.TRUE.equals(field.getIsFileRequired()) && StringUtils.isBlank(field.getFileDescription())) {
            return overSize(context, "{event.filedescription.null}", "fileDescription");
        }
        return true;
    }

    private boolean checkRolePermission(EventCreateRequest field, ConstraintValidatorContext context) {
        if (CollectionUtils.isNotEmpty(field.getQuestions())
                && field.getQuestions().size() > cacheService.getIntegerCacheValue(CacheConstants.QUESTION_LIMIT_FREE)) {
            Set<String> roles = jwtService.getUserRoles();
            if (roles.contains(RoleConstants.COMMERCIAL)) {
                return checkSizeByRole(field, context, CacheConstants.QUESTION_LIMIT_COMMERCIAL);
            } else if (roles.contains(RoleConstants.INDIVIDUAL)) {
                return checkSizeByRole(field, context, CacheConstants.QUESTION_LIMIT_IND);
            } else {
                return overSize(context, EVENT_QUESTION_OVERSIZE, QUESTIONS);
            }
        }
        return true;
    }

    private boolean checkSizeByRole(EventCreateRequest field, ConstraintValidatorContext context, String questionLimitCommercial) {
        if (field.getQuestions().size() > cacheService.getIntegerCacheValue(questionLimitCommercial)) {
            return overSize(context, EVENT_QUESTION_OVERSIZE, QUESTIONS);
        }
        return true;
    }

}
