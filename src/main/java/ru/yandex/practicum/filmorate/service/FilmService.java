package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmComparatorByLikes;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.DirectorRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private static final int COUNT = 10;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DirectorRepository directorRepository;

    public FilmService(@Qualifier("filmRepository") FilmStorage filmStorage,
                       @Qualifier("userRepository") UserStorage userStorage,
                       DirectorRepository directorRepository) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.directorRepository = directorRepository;
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
        filmToUpdate.setDirectors(film.getDirectors());
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

    public List<Film> getListOfPopularFilms(Integer count, Integer genreId, Integer year) {
        List<Film> films = filmStorage.findAll();

        List<Film> filterFilms = films.stream()
                .filter(film -> year == null || film.getReleaseDate().getYear() == year)
                .filter(film -> genreId == null ||
                        (film.hasGenres() && film.getGenres().stream()
                                .anyMatch(genre -> genre.getId().equals(genreId))))
                .toList();

        return filterFilms.stream()
                .sorted(new FilmComparatorByLikes().reversed())
                .limit(count != null && count > 0 ? count : COUNT)
                .collect(Collectors.toList());
    }

    public void deleteFilm(int filmId) {
        filmStorage.deleteFilmById(filmId);
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userStorage.findById(userId); // Проверка, что пользователь существует
        userStorage.findById(friendId); // Проверка, что друг существует

        return filmStorage.findCommonFilms(userId, friendId).stream()
                .sorted(new FilmComparatorByLikes().reversed())
                .toList();
    }

    public List<Film> getFilmsByDirector(int directorId, String sortBy) {
        directorRepository.findById(directorId);
        List<Film> films = filmStorage.findAll().stream()
                .filter(f -> f.hasDirectors() && f.getDirectors().stream()
                        .anyMatch(d -> d.getId() == directorId))
                .toList();

        if ("year".equals(sortBy)) {
            return films.stream()
                    .sorted(Comparator.comparing(Film::getReleaseDate))
                    .toList();
        } else {
            return films.stream()
                    .sorted(new FilmComparatorByLikes().reversed())
                    .toList();
        }
    }

    public List<Film> search(String query, String by) {
        return filmStorage.searchFilms(query, by).stream()
                .sorted((film1, film2) -> (film2.getLikes().size() - film1.getLikes().size()))
                .toList();
    }
}