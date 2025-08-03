package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserExtractor;

import java.util.*;

@Repository()
@Qualifier("userDbStorage")
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT u.user_id, " +
            "u.username, " +
            "u.login," +
            "u.email," +
            "u.birthday," +
            "f.friend_id AS friend " +
            "FROM users as u " +
            "LEFT JOIN friendship AS f ON f.user_id = u.user_id " +
            "ORDER BY u.user_id";
    private static final String FIND_BY_ID_QUERY = "SELECT u.user_id, " +
            "u.username, " +
            "u.login," +
            "u.email," +
            "u.birthday," +
            "f.friend_id AS friend " +
            "FROM users as u " +
            "LEFT JOIN friendship AS f ON f.user_id = u.user_id " +
            "WHERE u.user_id = ? " +
            "ORDER BY u.user_id";
    private static final String INSERT_QUERY = "INSERT INTO users (username, login, email, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET username = ?, login = ?, email = ?, birthday = ?" +
            "WHERE user_id = ?";
    private static final String ADD_FRIEND_QUERY = "MERGE INTO friendship USING " +
            "(VALUES (?, ?)) AS src (user_id, friend_id) " +
            "ON friendship.user_id = src.user_id AND friendship.friend_id = src.friend_id " +
            "WHEN MATCHED THEN " +
            "UPDATE SET friendship.friend_id = src.friend_id " +
            "WHEN NOT MATCHED THEN " +
            "INSERT (user_id, friend_id) VALUES (src.user_id, src.friend_id)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT u.user_id, " +
            "u.username, " +
            "u.login," +
            "u.email," +
            "u.birthday," +
            "f.friend_id AS friend " +
            "FROM users as u " +
            "LEFT JOIN friendship AS f ON f.user_id = u.user_id " +
            "WHERE u.user_id IN (SELECT friend_id FROM friendship WHERE user_id = ?) " +
            "ORDER BY u.user_id";
    private static final String GET_COMMON_FRIENDS = "SELECT u.user_id, " +
            "u.username, " +
            "u.login," +
            "u.email," +
            "u.birthday," +
            "f.friend_id AS friend " +
            "FROM users as u " +
            "LEFT JOIN friendship AS f ON f.user_id = u.user_id " +
            "WHERE u.user_id IN (SELECT friend_id FROM friendship WHERE user_id = ? AND friend_id IN (" +
            "SELECT friend_id FROM friendship WHERE user_id = ?)) " +
            "ORDER BY u.user_id";
    private static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM users WHERE user_id = ?";
    protected final UserExtractor userExtractor;
    public FilmStorage filmStorage;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper, UserExtractor userExtractor,
                          @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        super(jdbc, mapper);
        this.userExtractor = userExtractor;
        this.filmStorage = filmStorage;
    }

    public List<User> findAll() {
        return jdbc.query(FIND_ALL_QUERY, userExtractor);
    }

    public User findById(long id) {
        try {
            return jdbc.query(FIND_BY_ID_QUERY, userExtractor, id).getFirst();
        } catch (NoSuchElementException e) {
            throw new NotFoundException("User с id " + id + " не найден");
        }
    }

    public User create(User user) {
        long id = insert(INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    public User update(User user) {
        findById(user.getId());
        update(UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    public void addFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId); //проверка что оба юзера существуют
        update(ADD_FRIEND_QUERY, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public List<User> findFriends(long userId) {
        findById(userId);
        return jdbc.query(GET_FRIENDS_QUERY, userExtractor, userId);
    }

    public List<User> findCommonFriends(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        return jdbc.query(GET_COMMON_FRIENDS, userExtractor, userId, friendId);
    }

    public void deleteUserById(long id) {
        if (!delete(DELETE_USER_BY_ID_QUERY, id)) {
            throw new NotFoundException("Удаляемый пользователь не найден");
        }
    }

    public List<Film> getRecommendations(long userId) {
        User targetUser = findById(userId);

        Set<Long> targetLikes = new HashSet<>(filmStorage.findFilmLikes(targetUser));

        Collection<User> allUsers = findAll().stream()
                .filter(u -> u.getId() != userId)
                .toList();

        Map<User, Integer> similarity = new HashMap<>();

        for (User otherUser : allUsers) {
            Set<Long> otherLikes = new HashSet<>(filmStorage.findFilmLikes(otherUser));
            Set<Long> intersection = new HashSet<>(targetLikes);
            intersection.retainAll(otherLikes);
            similarity.put(otherUser, intersection.size());
        }

        if (similarity.isEmpty()) {
            return Collections.emptyList();
        }

        int maxSimilarity = similarity.values().stream().max(Integer::compareTo).orElse(0);

        if (maxSimilarity == 0) {
            return Collections.emptyList();
        }

        List<User> mostSimilarUsers = similarity.entrySet().stream()
                .filter(e -> e.getValue() == maxSimilarity)
                .map(Map.Entry::getKey)
                .toList();

        Set<Long> recommendedFilmIds = new HashSet<>();
        for (User similarUser : mostSimilarUsers) {
            Set<Long> likes = filmStorage.findFilmLikes(similarUser);
            likes.removeAll(targetLikes);
            recommendedFilmIds.addAll(likes);
        }

        return recommendedFilmIds.stream()
                .map(filmStorage::findById)
                .toList();
    }
}
