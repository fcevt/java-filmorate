package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmComparatorByLikes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.findAll();
    }

    public Film getFilmById(long id) {
        return filmStorage.findById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        Film filmToUpdate = filmStorage.findById(film.getId());
        filmToUpdate.setName(film.getName());
        if (film.hasDescription()) {
            filmToUpdate.setDescription(film.getDescription());
        }
        if (film.hasReleaseDate()) {
            filmToUpdate.setReleaseDate(film.getReleaseDate());
        }
        if (film.hasDuration()) {
            filmToUpdate.setDuration(film.getDuration());
        }
        if (film.hasMpa()) {
            filmToUpdate.setMpa(film.getMpa());
        }
        if (film.hasGenres()) {
            filmToUpdate.setGenres(film.getGenres());
        }
        return filmStorage.update(filmToUpdate);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.findById(filmId); // проверка что фильм существует
        userStorage.findById(userId); //  проверкf что юзер существует
        log.debug("пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.findById(filmId); // проверка что фильм существует
        userStorage.findById(userId); // проверка что юзер существует
        log.debug("пользователь с id {} удалил лайк с фильма с id {}", userId, filmId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getListOfPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(new FilmComparatorByLikes().reversed())
                .filter(Film::hasLikes)
                .limit(count)
                .toList();
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userStorage.findById(userId); // Проверка, что пользователь существует
        userStorage.findById(friendId); // Проверка, что друг существует

        return filmStorage.findCommonFilms(userId, friendId).stream()
                .sorted(new FilmComparatorByLikes().reversed())
                .toList();
    }
}
