package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class, UserExtractor.class, FilmDbStorage.class, FilmRowMapper.class,
        FilmExtractor.class})

public class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void createAndFindByIdUserTest() {
        User user = new User();
        user.setLogin("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("test@test.com");
        user.setName("test");
        user.setFriends(new HashSet<>());
        User user1 = userStorage.create(user);
        User user2 = userStorage.findById(user1.getId());
        Assertions.assertThat(user1).isEqualTo(user2);
    }

    @Test
    public void testFindAllUsers() {
        User user = new User();
        user.setLogin("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("test@test.com");
        user.setName("test");
        user.setFriends(new HashSet<>());
        User user1 = userStorage.create(user);
        List<User> users = userStorage.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setLogin("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("test@test.com");
        user.setName("test");
        user.setFriends(new HashSet<>());
        User user1 = userStorage.create(user);
        user1.setName("updateName");
        User user2 = userStorage.update(user1);
        Assertions.assertThat(user2.equals(userStorage.findById(user1.getId()))).isTrue();
    }

    @Test
    public void friendshipTest() {
        User user = new User();
        user.setLogin("test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setEmail("test@test.com");
        user.setName("test");
        user.setFriends(new HashSet<>());
        User user1 = userStorage.create(user);
        User user2 = new User();
        user2.setLogin("test1");
        user2.setBirthday(LocalDate.of(1990, 1, 1));
        user2.setEmail("test1@test.com");
        user2.setName("test11");
        user2.setFriends(new HashSet<>());
        User user3 = userStorage.create(user2);
        User user4 = new User();
        user4.setLogin("test2");
        user4.setBirthday(LocalDate.of(1990, 1, 1));
        user4.setEmail("test2@test.com");
        user4.setName("test22");
        user4.setFriends(new HashSet<>());
        User user5 = userStorage.create(user4);
        userStorage.addFriend(user5.getId(), user3.getId());
        userStorage.addFriend(user1.getId(), user3.getId());
        List<User> friends = userStorage.findCommonFriends(user5.getId(), user1.getId());
        Assertions.assertThat(friends.containsAll(userStorage.findFriends(user5.getId()))).isTrue();
        Assertions.assertThat(friends.containsAll(userStorage.findFriends(user1.getId()))).isTrue();
        Assertions.assertThat(userStorage.findFriends(user1.getId()).size() == 1).isTrue();
        Assertions.assertThat(userStorage.findFriends(user2.getId()).isEmpty()).isTrue();
        userStorage.removeFriend(user1.getId(), user3.getId());
        Assertions.assertThat(userStorage.findFriends(user1.getId()).isEmpty()).isTrue();
    }
}
