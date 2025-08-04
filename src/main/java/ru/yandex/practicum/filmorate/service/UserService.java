package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@Qualifier("userDbStorage")
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userRepository") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
        log.debug("пользователи с id {} и {} теперь друзья", userId, friendId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
        log.debug("Пользователи с id {} и {} больше не друзья", userId, friendId);
    }

    public List<User> getListOfFriends(Long userId) {
        return userStorage.findFriends(userId);
    }

    public List<User> getListOfMutualFriends(Long userId, Long friendId) {
        return userStorage.findCommonFriends(userId, friendId);
    }

    public User getUserById(Long userId) {
        return userStorage.findById(userId);
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        User userFromDb = userStorage.findById(user.getId());
        if (user.hasBirthday()) {
            userFromDb.setBirthday(user.getBirthday());
        }
        if (user.hasName()) {
            userFromDb.setName(user.getName());
        }
        userFromDb.setEmail(user.getEmail());
        userFromDb.setLogin(user.getLogin());
        return userStorage.update(userFromDb);
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }

    public List<Event> getEventsByUserId(Long userId) {
        return userStorage.getFeed(userId);
    }

    public List<Film> getRecommendations(long userId) {
        return userStorage.getRecommendations(userId);
    }
}
