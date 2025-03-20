package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Создан пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            User oldUser = users.get(user.getId());
            oldUser.setLogin(user.getLogin());
            oldUser.setBirthday(user.getBirthday());
            oldUser.setEmail(user.getEmail());
            oldUser.setName(user.getName());
            log.debug("Обновлен пользователь {}", oldUser);
            return oldUser;
        }
        log.warn("Пользователь с id={} не найден", user.getId());
        throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Логин содержит пробелы или пустой");
            throw new ValidationException("Логин не может содержать пробелы или быть пустым");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения задана в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getEmail() == null || (!user.getEmail().contains("@")) || user.getEmail().isBlank()) {
            log.warn("электронная почта пустая или не содержит символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }
}
