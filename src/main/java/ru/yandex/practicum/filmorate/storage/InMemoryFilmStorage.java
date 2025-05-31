package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MinDateAnnotationValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long id = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override public Film findById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Film not found");
        }
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        MinDateAnnotationValidator.validateDate(film);
        film.setId(++id);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("создан фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            MinDateAnnotationValidator.validateDate(film);
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
}
