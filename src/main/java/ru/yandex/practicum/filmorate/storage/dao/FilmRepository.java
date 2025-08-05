package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.FilmExtractor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.dao.ReviewRepository.INSERT_EVENT_QUERY;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Qualifier("filmDbStorage")
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT f.film_id, " +
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
            " ORDER BY f.film_id";
    private static final String FIND_BY_ID_QUERY = "SELECT f.film_id, " +
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
            " WHERE f.film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films (film_name, description, release_date, duration," +
            "rating_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET film_name = ?, description = ?, release_date = ?," +
            "duration = ?, rating_id = ? WHERE film_id = ?";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String UPDATE_FILM_GENRE_QUERY = "MERGE INTO film_genre USING " +
            "(VALUES (?, ?)) AS src (film_id, genre_id) " +
            "ON film_genre.film_id = src.film_id AND film_genre.genre_id = src.genre_id " +
            "WHEN MATCHED THEN " +
            "UPDATE SET film_genre.genre_id = src.genre_id " +
            "WHEN NOT MATCHED THEN " +
            "INSERT (film_id, genre_id) VALUES (src.film_id, src.genre_id)";
    private static final String DELETE_FILM_GENRE_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String INSERT_LIKE_QUERY = "MERGE INTO likes USING " +
            "(VALUES (?, ?)) AS src (film_id, user_id) " +
            "ON likes.film_id = src.film_id AND likes.user_id = src.user_id " +
            "WHEN MATCHED THEN " +
            "UPDATE SET likes.user_id = src.user_id " +
            "WHEN NOT MATCHED THEN " +
            "INSERT (film_id, user_id) VALUES (src.film_id, src.user_id)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? and user_id = ?";
    private static final String DELETE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String INSERT_FILM_DIRECTOR_QUERY = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
    private static final String UPDATE_FILM_DIRECTOR_QUERY = "MERGE INTO film_director USING " +
            "(VALUES (?, ?)) AS src (film_id, director_id) " +
            "ON film_director.film_id = src.film_id AND film_director.director_id = src.director_id " +
            "WHEN MATCHED THEN " +
            "UPDATE SET film_director.director_id = src.director_id " +
            "WHEN NOT MATCHED THEN " +
            "INSERT (film_id, director_id) VALUES (src.film_id, src.director_id)";
    private static final String DELETE_FILM_DIRECTORS_QUERY = "DELETE FROM film_director WHERE film_id = ?";
    private static final String FIND_COMMON_FILMS_QUERY = "SELECT f.film_id, " +
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
            "WHERE f.film_id IN (" +
            "SELECT l1.film_id FROM likes l1 WHERE l1.user_id = ? " +
            "INTERSECT " +
            "SELECT l2.film_id FROM likes l2 WHERE l2.user_id = ?" +
            ") " +
            "ORDER BY f.film_id";
    private static final String FIND_FILM_LIKES = "SELECT film_id FROM likes WHERE user_id = ?";
    private static final String DIRECTOR = "director";
    private static final String TITLE = "title";
    private static final String SEARCH_TEMPLATE = "%s ILIKE '%%%s%%'";

    protected final FilmExtractor filmExtractor;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, FilmExtractor filmExtractor) {
        super(jdbc, mapper);
        this.filmExtractor = filmExtractor;
    }

    public List<Film> findAll() {
        return jdbc.query(FIND_ALL_QUERY, filmExtractor);
    }

    public Film findById(long id) {
        try {
            return jdbc.query(FIND_BY_ID_QUERY, filmExtractor, id).getFirst();
        } catch (NoSuchElementException e) {
            throw new NotFoundException("фильм с id : " + id + "не найден");
        }
    }

    public Film create(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        if (film.hasGenres()) {
            Set<Genre> sortedGenres = film.getGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(sortedGenres);
            for (Genre genre : film.getGenres()) {
                insert(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
            }
        }
        if (film.hasDirectors()) {
            for (Director director : film.getDirectors()) {
                update(INSERT_FILM_DIRECTOR_QUERY, film.getId(), director.getId());
            }
        }

        return film;
    }

    public Film update(Film film) {
        findById(film.getId());
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.hasGenres()) {
            Set<Genre> sortedGenres = film.getGenres().stream().sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(sortedGenres);
            delete(DELETE_FILM_GENRE_QUERY, film.getId());
            for (Genre genre : film.getGenres()) {
                update(UPDATE_FILM_GENRE_QUERY, film.getId(), genre.getId());
            }
        }
        if (film.hasDirectors()) {
            delete(DELETE_FILM_DIRECTORS_QUERY, film.getId());
            for (Director director : film.getDirectors()) {
                update(UPDATE_FILM_DIRECTOR_QUERY, film.getId(), director.getId());
            }
        } else delete(DELETE_FILM_DIRECTORS_QUERY, film.getId());

        return film;
    }

    public void addLike(long filmId, long userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "LIKE",
                "ADD",
                filmId
        );
    }

    public void deleteLike(long filmId, long userId) {
        jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
        insert(INSERT_EVENT_QUERY,
                Timestamp.from(Instant.now()),
                userId,
                "LIKE",
                "REMOVE",
                filmId
        );
    }

    @Override
    public List<Film> findCommonFilms(long userId, long friendId) {
        return jdbc.query(FIND_COMMON_FILMS_QUERY, filmExtractor, userId, friendId);
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        StringBuilder sqlQuery = new StringBuilder();
        Set<String> params = Set.of(by.split(","));

        if (params.contains(DIRECTOR))
            sqlQuery.append(SEARCH_TEMPLATE.formatted("d.director_name", query));
        if (params.contains(TITLE)) {
            if (!sqlQuery.isEmpty())
                sqlQuery.append(" OR ");
            sqlQuery.append(SEARCH_TEMPLATE.formatted("f.film_name", query));
        }

        if (sqlQuery.isEmpty())
            return List.of();

        sqlQuery.insert(0, " WHERE ");
        sqlQuery.insert(0, FIND_ALL_QUERY.substring(0, FIND_ALL_QUERY.indexOf("ORDER BY")));
        return jdbc.query(sqlQuery.toString(), filmExtractor);
    }

    public void deleteFilmById(long filmId) {
        if (!delete(DELETE_FILM_BY_ID_QUERY, filmId)) {
            throw new NotFoundException("Удаляемый Фильм не найден");
        }
    }

    @Override
    public Set<Long> findFilmLikes(User user) {
        return new HashSet<>(jdbc.queryForList(FIND_FILM_LIKES, Long.class, user.getId()));
    }
}
