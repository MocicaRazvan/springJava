package com.moc.wellness.validations;

import com.moc.wellness.validations.validators.SortingCriteriaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortingCriteriaValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SortingCriteria {
    String message() default "Invalid sorting criteria";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
