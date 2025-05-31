package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MinDateAnnotationValidator;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
//    private long id = 0;
//    private final Map<Long, Film> films = new HashMap<>();


    @GetMapping
//    public List<Film> findAll() {
//        return new ArrayList<>(films.values());
//    }

    @PostMapping
//    public Film create(@Valid @RequestBody Film film) {
//        MinDateAnnotationValidator.validateDate(film);
//        film.setId(++id);
//        films.put(film.getId(), film);
//        log.debug("создан фильм {}", film);
//        return film;
//    }

    @PutMapping
//    public Film update(@Valid @RequestBody Film film) {
//        if (films.containsKey(film.getId())) {
//            MinDateAnnotationValidator.validateDate(film);
//            Film oldFilm = films.get(film.getId());
//            oldFilm.setName(film.getName());
//            oldFilm.setDescription(film.getDescription());
//            oldFilm.setDuration(film.getDuration());
//            oldFilm.setReleaseDate(film.getReleaseDate());
//            log.debug("фильм обновлен {}", oldFilm);
//            return oldFilm;
//        }
//        log.warn("Фильм с id={} не найден", film.getId());
//        throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
//    }
}
