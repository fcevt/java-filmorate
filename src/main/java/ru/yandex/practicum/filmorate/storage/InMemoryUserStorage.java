package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.debug("Создан пользователь {}", user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id={} не найден", user.getId());
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        User oldUser = users.get(user.getId());
        user.setFriends(oldUser.getFriends());
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);
        return user;
    }

    public User findById(long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователя с id={} не найдено", id);
            throw new NotFoundException("Пользователь с id=" + id + "не найден");
        }
        return users.get(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
    }

    @Override
    public void removeFriend(long userId, long friendId) {
    }

    @Override
    public List<User> findFriends(long userId) {
        return List.of();
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        return List.of();
    }

    @Override
    public List<Event> getFeed(long userId) {
        return List.of();
    }
}
