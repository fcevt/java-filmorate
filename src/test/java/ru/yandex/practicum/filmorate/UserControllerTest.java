package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {

    UserController userController = new UserController();

    @Test
    public void creatingUserWithoutNameTest() {
        User user = new User();
        user.setEmail("wers@mail.ru");
        user.setBirthday(LocalDate.of(2003, 12, 1));
        user.setLogin("fgjd");
        User user1 = userController.create(user);
        Assertions.assertEquals(user.getBirthday(), user1.getBirthday());
        Assertions.assertEquals(user.getEmail(), user1.getEmail());
        Assertions.assertEquals(user.getLogin(),user1.getLogin());
        Assertions.assertEquals(user.getLogin(),user1.getName());
    }

    @Test
    public void loginValidationWithSpacesTest() {
        User user = new User();
        user.setEmail("wers@mail.ru");
        user.setBirthday(LocalDate.of(2003, 12, 1));
        user.setLogin("fgj d");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void invalidEmailValidationTest() {
        User user = new User();
        user.setEmail("wersmail");
        user.setBirthday(LocalDate.of(2003, 12, 1));
        user.setLogin("fgjd");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void dateOfBirthValidationTest() {
        User user = new User();
        user.setEmail("wers@mail.ru");
        user.setBirthday(LocalDate.now());
        user.setLogin("fgjd");
        User user1 = userController.create(user);
        Assertions.assertEquals(user.getBirthday(), user1.getBirthday());
        Assertions.assertEquals(user.getEmail(), user1.getEmail());
        Assertions.assertEquals(user.getLogin(),user1.getLogin());
        Assertions.assertEquals(user.getLogin(),user1.getName());
        user.setBirthday(LocalDate.now().plusDays(1));
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }
}

