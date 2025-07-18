package ru.yandex.practicum.filmorate.model.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MinDateAnnotationValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinDate {
    String message() default "Не верная дата релиза";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value();
}
