package ru.yandex.practicum.filmorate.model.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinDateAnnotationValidator implements ConstraintValidator<MinDate, LocalDate> {
    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate minDate;

    @Override
    public void initialize(final MinDate constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value(), dateTimeFormatter);
    }

    @Override
    public boolean isValid(LocalDate contactField, ConstraintValidatorContext context) {
        return !contactField.isBefore(minDate);
    }
}
