package ru.pcs.crowdfunding.client.validation;

import ru.pcs.crowdfunding.client.dto.SignUpForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NamesValidator implements ConstraintValidator<NotSameNames, SignUpForm> {
    @Override
    public boolean isValid(SignUpForm object, ConstraintValidatorContext context) {
        return !object.getFirstName().equals(object.getLastName());
    }
}
