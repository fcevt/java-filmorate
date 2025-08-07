package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.EventRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserExtractor;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserLikeExtractor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.dao.ReviewRepository.INSERT_EVENT_QUERY;

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
    private static final String GET_FEED_QUERY = "SELECT * FROM feed WHERE user_id = ?";
    private static final String GET_USER_LIKES_QUERY = "SELECT u.user_id, " +
            "u.username, " +
            "u.login, " +
            "u.email, " +
            "u.birthday, " +
            "l.film_id " +
            "FROM users AS u " +
            "LEFT JOIN likes AS l ON u.user_id = l.user_id ";
    private static final String GET_FILMS_FOR_RECOMMENDATION = "SELECT f.film_id, " +
            "f.film_name, " +
            "f.description, " +
            "f.duration, " +
            "f.release_date, " +
            "r.rating_id, " +
            "r.code, " +
            "r.description AS mpa_description, " +
            "g.genre_name,  " +
            "l.user_id AS likes, " +
            "fg.genre_id, " +
            "d.director_id, " +
            "d.director_name " +
            "FROM films AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
            "LEFT JOIN film_genre AS fg ON fg.film_id = f.film_id " +
            "LEFT JOIN genre AS g ON g.genre_id = fg.genre_id " +
            "LEFT JOIN film_director AS fd ON fd.film_id = f.film_id " +
            "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
            "WHERE f.film_id IN (%s)";
    protected final UserExtractor userExtractor;
    protected  final EventRowMapper eventRowMapper;
    protected FilmStorage filmStorage;
    protected final UserLikeExtractor userLikeExtractor;
    protected final FilmExtractor filmExtractor;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper, UserExtractor userExtractor,
                          @Qualifier("filmDbStorage") FilmStorage filmStorage, EventRowMapper eventRowMapper,
                          UserLikeExtractor userLikeExtractor, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.userExtractor = userExtractor;
        this.eventRowMapper = eventRowMapper;
        this.filmStorage = filmStorage;
        this.userLikeExtractor = userLikeExtractor;
        this.filmExtractor = filmExtractor;
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
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "FRIEND",
                "ADD",
                friendId
        );
    }

    public void removeFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "FRIEND",
                "REMOVE",
                friendId
        );
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

    public List<Event> getFeed(long userId) {
        findById(userId);
        return jdbc.query(GET_FEED_QUERY, eventRowMapper, userId);
    }

    public List<Film> getRecommendations(long userId) {
        User targetUser = findById(userId);
        Map<User, Set<Long>> userLikes = jdbc.query(GET_USER_LIKES_QUERY, userLikeExtractor);

        Set<Long> targetLikes = userLikes.get(targetUser);

        List<User> allUsers = userLikes.keySet().stream().filter(u -> u.getId() != userId).toList();
        Map<User, Integer> similarity = new HashMap<>();

        for (User otherUser : allUsers) {
            Set<Long> otherLikes = userLikes.get(otherUser);
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
            Set<Long> likes = userLikes.get(similarUser);
            likes.removeAll(targetLikes);
            recommendedFilmIds.addAll(likes);
        }

        String newSql = String.join(",", Collections.nCopies(recommendedFilmIds.size(), "?"));
        Object[] ids = recommendedFilmIds.toArray();

        return jdbc.query(String.format(GET_FILMS_FOR_RECOMMENDATION, newSql), ids, filmExtractor);
    }
}
