package com.example.blog.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatchesValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Matches {
    String property();
    String verifier();
    String message() default "{com.example.blog.validation.Matches}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
