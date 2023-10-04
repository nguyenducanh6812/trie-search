package com.example.peer39.urlcore.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UrlListValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrlList {
    String message() default "Invalid URL list";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}