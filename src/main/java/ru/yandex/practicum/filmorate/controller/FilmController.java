package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public static final int MAX_LENGTH_DESCRIPTION = 200;
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long id = 0;
    private final Map<Long, Film> films = new HashMap<>();


    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("создан фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            Film oldFilm = films.get(film.getId());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setReleaseDate(film.getReleaseDate());
            log.debug("фильм обновлен {}", oldFilm);
            return oldFilm;
        }
        log.warn("Фильм с id={} не найден", film.getId());
        throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");

    }

    private void validateFilm(Film film) {
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.warn("длина описания превышает 200 символов");
            throw new ValidationException("Максимальная длина описания не может превышать 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза задана раньше 28.12.1985");
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1985");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма задана отрицательным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Не указано название фильма");
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }
}
