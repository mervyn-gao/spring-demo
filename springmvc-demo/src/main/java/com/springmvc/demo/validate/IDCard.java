package com.springmvc.demo.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by mengran.gao on 2017/8/8.
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDCardValidator.class)
public @interface IDCard {

    String message() default "身份证号不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
