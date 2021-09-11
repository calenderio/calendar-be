/*
 * @author : Oguz Kahraman
 * @since : 8.09.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = SchedulerDetailValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchedulerDetail {

    String message() default "{scheduler.empty.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
