package com.example.blog.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String property();
    String message() default "{com.example.blog.validation.Unique}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
