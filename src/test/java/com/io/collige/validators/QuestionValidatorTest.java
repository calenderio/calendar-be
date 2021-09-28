/*
 * @author : Oguz Kahraman
 * @since : 27.09.2021
 *
 * Copyright - Collige App Java API
 **/
package com.io.collige.validators;

import com.io.collige.enums.QuestionType;
import com.io.collige.models.internals.QuestionModel;
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
class QuestionValidatorTest {

    @InjectMocks
    private QuestionValidator questionValidator;

    @Test
    void isValid_false() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        QuestionModel model = new QuestionModel();
        model.setType(QuestionType.TEXT);
        boolean response = questionValidator.isValid(model, context);
        assertFalse(response);
    }

    @Test
    void isValid_lengthFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        QuestionModel model = new QuestionModel();
        model.setType(QuestionType.TEXT);
        model.setLengthMin(10);
        model.setLengthMax(9);
        boolean response = questionValidator.isValid(model, context);
        assertFalse(response);
    }

    @Test
    void isValid_boolFalse() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node
                = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        QuestionModel model = new QuestionModel();
        model.setType(QuestionType.BOOL);
        boolean response = questionValidator.isValid(model, context);
        assertFalse(response);
    }

    @Test
    void isValid() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        QuestionModel model = new QuestionModel();
        model.setType(QuestionType.TEXT);
        model.setLengthMin(9);
        model.setLengthMax(10);
        boolean response = questionValidator.isValid(model, context);
        assertTrue(response);
    }

}