package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.service.ReviewService.INSERT_EVENT_QUERY;

@Repository
public class ReviewRepository extends BaseRepository<Review> implements ReviewStorage {
    private static final String INSERT_QUERY = "INSERT INTO reviews (film_id, user_id, content, positiv) " +
            "VALUES(?, ?, ?, ?)";
    private static final String FIND_ONE = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "(SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ) AS useful " +
            "FROM reviews AS r " +
            "WHERE r.id = ?";
    private static final String FIND_REVIEW_BY_FILM = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "IFNULL ((SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ), 0) AS useful " +
            "FROM reviews AS r " +
            "WHERE r.film_id = ? " +
            "ORDER BY useful DESC";
    private static final String FIND_ALL_REVIEW = "SELECT r.id, " +
            "r.film_id, " +
            "r.user_id, " +
            "r.content, " +
            "r.positiv, " +
            "IFNULL ((SELECT sum(like_value) " +
            "FROM reviews_likes " +
            "WHERE review_id = r.id ), 0) AS useful " +
            "FROM reviews AS r " +
            "ORDER BY useful DESC";
    private static final String DELETE = "DELETE FROM reviews WHERE id = ?";
    private static final String INSERT_LIKE_VALUE = "INSERT INTO reviews_likes (review_id, user_id, like_value) " +
            "VALUES(?, ?, ?)";
    private static final String MODIFY_LIKE_VALUE = "INSERT INTO reviews_likes (review_id, user_id, like_value) " +
            "VALUES(?, ?, ?)";
    private static final String DELETE_LIKE_VALUE = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ? " +
            "AND like_value = ?";
    private static final String UPDATE_QUERY = "UPDATE reviews SET film_id = ?, user_id = ?, content = ?, " +
            "positiv = ? WHERE id = ?";

    private static final String FIND_LIKE_BY_ID_USER =
            "SELECT COUNT(*) FROM reviews_likes WHERE review_id = ? AND user_id = ?";
    private static final String FIND_LIKE_BY_ID_USER_VALUE =
            "SELECT COUNT(*) FROM reviews_likes WHERE review_id = ? AND user_id = ? AND like_value = ?";
    private static final String UPDATE_LIKE_VALUE =
            "UPDATE reviews_likes SET like_value = ? WHERE review_id = ? AND user_id = ?";

    public ReviewRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review create(Review review) {
        review.setId(insert(INSERT_QUERY,
                review.getFilm(),
                review.getUser(),
                review.getContent(),
                review.isPositive()));
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                review.getUser(),
                "REVIEW",
                "ADD",
                review.getId());
        return review;
    }

    @Override
    public Optional<Review> get(Long id) {
        return findOne(FIND_ONE, id);
    }

    @Override
    public List<Review> getMany(Long id) {
        if (id != null)
            return findMany(FIND_REVIEW_BY_FILM, id);
        else
            return findMany(FIND_ALL_REVIEW);
    }

    @Override
    public void delete(Long id) {
        Review review = findOne(FIND_ONE, id).get();
        delete(DELETE, id);
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                review.getUser(),
                "REVIEW",
                "REMOVE",
                id);
    }

    @Override
    public void setLikeValue(Long id, Long userId, Integer value) {
        insert(INSERT_LIKE_VALUE, id, userId, value);
        String operation = value > 1 ? "REMOVE" : "ADD";
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "LIKE",
                operation,
                id);
    }

    @Override
    public void deleteLikeValue(Long id, Long userId, Integer value) {
        delete(DELETE_LIKE_VALUE, id, userId, value);
        String operation = value < 1 ? "REMOVE" : "ADD";
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "LIKE",
                operation,
                id);
    }

    @Override
    public Review update(Review review) {
        update(UPDATE_QUERY,
                review.getFilm(),
                review.getUser(),
                review.getContent(),
                review.isPositive(),
                review.getId());
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                review.getUser(),
                "REVIEW",
                "UPDATE",
                review.getId());
        return review;
    }

    @Override
    public void updateLikeValue(Long id, Long userId, Integer value) {
        update(UPDATE_LIKE_VALUE, value, id, userId);
        String operation = value < 1 ? "REMOVE" : "ADD";
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "LIKE",
                operation,
                id);
    }

    @Override
    public boolean findExistLikeForReviewUser(Long review, Long user) {
        return jdbc.queryForObject(FIND_LIKE_BY_ID_USER, Integer.class,
                new Object[]{review, user}) != 0;
    }

    @Override
    public boolean findExistLikeForReviewUserValue(Long review, Long user, Integer value) {
        return jdbc.queryForObject(FIND_LIKE_BY_ID_USER_VALUE, Integer.class,
                new Object[]{review, user, value}) != 0;
    }

}
