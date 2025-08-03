package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Repository
public class DirectorRepository extends BaseRepository<Director> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM directors";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM directors WHERE director_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO directors (director_name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE directors SET director_name = ? WHERE director_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM directors WHERE director_id = ?";
    private static final String FIND_FILM_DIRECTORS = "SELECT d.director_id, d.director_name FROM film_director fd " +
            "JOIN directors d ON fd.director_id = d.director_id WHERE fd.film_id = ?";
    private static final String INSERT_FILM_DIRECTOR = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
    private static final String DELETE_FILM_DIRECTORS = "DELETE FROM film_director WHERE film_id = ?";

    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Director findById(int id) {
        Optional<Director> director = findOne(FIND_BY_ID_QUERY, id);
        if (director.isEmpty()) {
            throw new NotFoundException("Режиссер с id: " + id + " не найден");
        }
        return director.get();
    }

    public Director create(Director director) {
        long id = insert(INSERT_QUERY, director.getName());
        director.setId((int) id);
        return director;
    }

    public Director update(Director director) {
        findById(director.getId());
        update(UPDATE_QUERY, director.getName(), director.getId());
        return director;
    }

    public boolean delete(int id) {
        return delete(DELETE_QUERY, id);
    }

    public List<Director> findFilmDirectors(int filmId) {
        return findMany(FIND_FILM_DIRECTORS, filmId);
    }

    public void addFilmDirector(int filmId, int directorId) {
        update(INSERT_FILM_DIRECTOR, filmId, directorId);
    }

    public void deleteFilmDirectors(int filmId) {
        jdbc.update(DELETE_FILM_DIRECTORS, filmId);
    }
}