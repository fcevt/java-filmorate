package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

@Repository
public class MPARepository extends BaseRepository<MPA> {
    private static final String FIND_ALL_RATINGS_QUERY = "SELECT * FROM rating";
    private static final String FIND_RATING_BY_ID_QUERY = "SELECT * FROM rating WHERE rating_id = ?";

    public MPARepository(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    public List<MPA> findAllRatings() {
        return findMany(FIND_ALL_RATINGS_QUERY);
    }

    public MPA findRatingById(int id) {
        Optional<MPA> rating = findOne(FIND_RATING_BY_ID_QUERY, id);
        if (rating.isEmpty()) {
            throw new NotFoundException("Рейтинг с  id: " + id + " не найден");
        }
        return rating.get();
    }
}
