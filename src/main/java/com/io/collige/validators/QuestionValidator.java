/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Collige Java API
 **/
package com.io.collige.validators;

import com.io.collige.enums.QuestionType;
import com.io.collige.models.internals.event.QuestionModel;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class QuestionValidator implements ConstraintValidator<QuestionCheck, QuestionModel> {

    @Override
    public boolean isValid(QuestionModel field, ConstraintValidatorContext context) {
        if (!QuestionType.BOOL.equals(field.getType())) {
            if (field.getLengthMax() == null || field.getLengthMin() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{event.question.minmax}")
                        .addPropertyNode("type").addConstraintViolation();
                return false;
            } else if (field.getLengthMax() < field.getLengthMin()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{event.question.maxmin}")
                        .addPropertyNode("type").addConstraintViolation();
                return false;
            }
        } else {
            if (StringUtils.isBlank(field.getValues())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{event.question.values}")
                        .addPropertyNode("type").addConstraintViolation();
                return false;
            }
        }
        return true;

    }

}
