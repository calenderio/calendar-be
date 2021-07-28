/*
 * @author : Oguz Kahraman
 * @since : 28.02.2021
 *
 * Copyright - Deity Arena Java API
 **/
package com.io.calendar.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "{error.password.match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
