package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long id = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(long id) {
        if (!films.containsKey(id)) {
            log.warn("Film with id {} not found", id);
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        return films.get(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Film create(Film film) {
        film.setId(++id);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("создан фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с id={} не найден", film.getId());
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        film.setLikes(films.get(film.getId()).getLikes());
        films.put(film.getId(), film);
        log.debug("фильм обновлен {}", film);
        return film;
    }

    @Override
    public List<Film> findCommonFilms(long userId, long friendId) {
        return films.values().stream()
                .filter(film -> film.getLikes().contains(userId))
                .filter(film -> film.getLikes().contains(friendId))
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .collect(Collectors.toList());
    }
}
