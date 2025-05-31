package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFromFriends(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getListOfFriends(Long userId) {
        return userStorage.findById(userId).getFriends().stream()
                .map(friendId -> userStorage.findById(friendId))
                .toList();
    }

    public List<User> getListOfMutualFriends(Long userId, Long friendId) {
        return userStorage.findById(userId).getFriends().stream()
                .filter(friend -> userStorage.findById(friendId).getFriends().contains(friend))
                .map(friend1 -> userStorage.findById(friend1))
                .toList();
    }
}
