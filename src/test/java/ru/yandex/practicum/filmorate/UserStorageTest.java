package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

public class UserStorageTest {

    final UserStorage userStorage = new InMemoryUserStorage();

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

