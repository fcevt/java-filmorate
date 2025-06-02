package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmComparatorByLikes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.findById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId); //для проверки, что юзер существует
        log.debug("пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId); //для проверки, что юзер существует
        log.debug("пользователь с id {} удалил лайк с фильма с id {}", userId, filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getListOfPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(new FilmComparatorByLikes().reversed())
                .filter(film -> !film.getLikes().isEmpty())
                .limit(count)
                .toList();
    }
}
