package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.annotation.NotContainSpaces;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @NotContainSpaces(message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @EqualsAndHashCode.Exclude
    private Set<Long> friends = new HashSet<>();

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasBirthday() {
        return birthday != null;
    }
}
