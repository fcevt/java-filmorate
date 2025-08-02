package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Film, Set<Genre>> filmGenre = new LinkedHashMap<>();
        Map<Film, Set<Long>> filmLikes = new LinkedHashMap<>();
        Map<Film, Set<Director>> filmDirectors = new LinkedHashMap<>();
        while (rs.next()) {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setDuration(rs.getInt("duration"));
            film.setReleaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate());
            MPA mpa = new MPA();
            if (rs.getInt("rating_id") != 0) {
                mpa.setId(rs.getInt("rating_id"));
                mpa.setName(rs.getString("code"));
                mpa.setDescription(rs.getString("mpa_description"));
            }
            film.setMpa(mpa);
            filmGenre.putIfAbsent(film, new LinkedHashSet<>());
            filmLikes.putIfAbsent(film, new LinkedHashSet<>());
            filmDirectors.putIfAbsent(film, new LinkedHashSet<>());
            Director director = new Director();
            if (rs.getInt("director_id") != 0) {
                director.setId(rs.getInt("director_id"));
                director.setName(rs.getString("director_name"));
            }
            if (director.getId() != null) {
                filmDirectors.get(film).add(director);
            }
            Genre genre = new Genre();
            if (rs.getInt("genre_id") != 0) {
                genre.setId(rs.getInt("genre_id"));
                genre.setName(rs.getString("genre_name"));
            }
            if (genre.getId() != null) {
                filmGenre.get(film).add(genre);
            }
            long like = rs.getLong("likes");
            if (like != 0) {
                filmLikes.get(film).add(like);
            }
        }
        return filmGenre.keySet().stream()
                .peek(film -> film.setLikes(filmLikes.get(film)))
                .peek(film -> film.setGenres(filmGenre.get(film).stream()
                        .sorted(Comparator.comparing(Genre::getId))
                            .collect(Collectors.toCollection(LinkedHashSet::new))))
                .peek(film -> {
                    if (film.getGenres() == null) {
                        film.setGenres(new HashSet<>());
                    }
                })
                .peek(film -> film.setDirectors(filmDirectors.get(film)))
                .peek(film -> {
                    if (film.getDirectors() == null) {
                        film.setDirectors(new HashSet<>());
                    }
                })
                .toList();
    }
}
