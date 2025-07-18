package ru.yandex.practicum.filmorate.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<NotContainSpaces, String> {

    @Override
    public void initialize(NotContainSpaces constraintAnnotation) {
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext context) {
        return !contactField.contains(" ");
    }
}
