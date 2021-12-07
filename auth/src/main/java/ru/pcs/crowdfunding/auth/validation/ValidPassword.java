package ru.pcs.crowdfunding.auth.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "invalid password or password must contains !, # , @, $ or %.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
