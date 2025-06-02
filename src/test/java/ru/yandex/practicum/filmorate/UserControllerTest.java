package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

class UserControllerTest {

    UserStorage userStorage = new InMemoryUserStorage();

    @Test
    public void loginValidationWithSpacesTest() {
        User user = new User();
        user.setEmail("wers@mail.ru");
        user.setBirthday(LocalDate.of(2003, 12, 1));
        user.setLogin("fgj d");
        Assertions.assertThrows(ValidationException.class, () -> userStorage.create(user));
    }

    @Test
    public void dateOfBirthValidationTest() {
        User user = new User();
        user.setEmail("wers@mail.ru");
        user.setBirthday(LocalDate.now());
        user.setLogin("fgjd");
        User user1 = userStorage.create(user);
        Assertions.assertEquals(user.getBirthday(), user1.getBirthday());
        Assertions.assertEquals(user.getEmail(), user1.getEmail());
        Assertions.assertEquals(user.getLogin(),user1.getLogin());
        Assertions.assertEquals(user.getLogin(),user1.getName());
    }
}

