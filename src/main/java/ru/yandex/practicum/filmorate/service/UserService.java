package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        log.debug("пользователи с id {} и {} теперь друзья", userId, friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        log.debug("Пользователи с id {} и {} больше не друзья", userId, friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getListOfFriends(Long userId) {
        return userStorage.findById(userId).getFriends().stream()
                .map(userStorage::findById)
                .toList();
    }

    public List<User> getListOfMutualFriends(Long userId, Long friendId) {
        return userStorage.findById(userId).getFriends().stream()
                .filter(friend -> userStorage.findById(friendId).getFriends().contains(friend))
                .map(userStorage::findById)
                .toList();
    }

    public User getUserById(Long userId) {
        return userStorage.findById(userId);
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }
}
