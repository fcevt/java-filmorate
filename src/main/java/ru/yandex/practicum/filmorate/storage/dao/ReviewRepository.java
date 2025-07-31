package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Optional;

@Repository
public class ReviewRepository extends BaseRepository<Review> implements ReviewStorage {
    private static final String INSERT_QUERY = "INSERT INTO reviews (film_id, user_id, content, positiv) " +
            "VALUES(?, ?, ?, ?)";
    private static final String FIND_ONE = "SELECT r.id, " +
                                                  "r.film_id, " +
                                                  "r.user_id, " +
                                                  "r.content, " +
                                                  "r.positiv, " +
                                                  "(SELECT SUM(LIKE_VALUE) " +
                                                      "FROM REVIEWS_LIKES " +
                                                  "WHERE REVIEW_ID = r.ID ) AS useful " +
                                           "FROM REVIEWS AS r " +
                                           "WHERE r.id = ?";

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

        return review;
    }

    @Override
    public Optional<Review> get(Long id) {
        return findOne(FIND_ONE, id);
    }
}
