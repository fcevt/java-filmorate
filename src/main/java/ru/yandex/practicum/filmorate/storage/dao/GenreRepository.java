package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository  extends BaseRepository<Genre> {
    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM genre";
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAllGenres() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    public Genre findGenreById(int id) {
        Optional<Genre> genre = findOne(FIND_GENRE_BY_ID, id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Genre not found");
        }
        return genre.get();
    }
}
