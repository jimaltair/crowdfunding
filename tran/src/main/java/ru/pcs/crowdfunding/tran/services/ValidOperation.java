package ru.pcs.crowdfunding.tran.services;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OperationValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOperation {
//    String message() default "invalid password or password must contains !, # , @, $ or %.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default  {};
}
