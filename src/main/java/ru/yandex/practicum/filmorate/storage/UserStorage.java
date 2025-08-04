package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User update(User user);

    User findById(long id);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> findFriends(long userId);

    List<User> findCommonFriends(long userId, long friendId);

    void deleteUserById(long id);

    List<Event> getFeed(long userId);

    List<Film> getRecommendations(long userId);
}
