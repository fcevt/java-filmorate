package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(long id);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    void deleteFilmById(long id);

    List<Film> findCommonFilms(long userId, long friendId);

    Set<Long> findFilmLikes(User user);

    List<Film> searchFilms(String query, String by);
}
