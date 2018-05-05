package com.tracker.config.validation.username;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Perebeinis on 04.05.2018.
 */
@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameConstraint {
    String message() default "userAlreadyExist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
