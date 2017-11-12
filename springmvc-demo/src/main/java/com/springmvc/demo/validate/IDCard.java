package com.springmvc.demo.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by mengran.gao on 2017/8/8.
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDCardValidator.class)
public @interface IDCard {

    String message() default "{com.springmvc.demo.validate.IDCard.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
