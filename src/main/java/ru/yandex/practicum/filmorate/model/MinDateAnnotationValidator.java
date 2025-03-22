package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class MinDateAnnotationValidator {

    public static void validateDate(Object object) {
       Class<?> objectClass = object.getClass();
       DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
       for (Field field : objectClass.getDeclaredFields()) {
           if (field.isAnnotationPresent(MinDate.class)) {
               MinDate annotation = field.getAnnotation(MinDate.class);
               LocalDate expectedMinDate = LocalDate.parse(annotation.value(), dateTimeFormatter);
               field.setAccessible(true);
               try {
                   Object fieldValue = field.get(object);
                   LocalDate date = (LocalDate) fieldValue;
                   if (date.isBefore(expectedMinDate)) {
                       log.warn("Задана дата раньше 28.12.1895");
                       throw new ValidationException("Дата релиза не может быть раньше 28.12.1895");
                   }
               } catch (IllegalAccessException e) {
                   log.warn(e.getMessage());
               }
           }
       }
    }
}
