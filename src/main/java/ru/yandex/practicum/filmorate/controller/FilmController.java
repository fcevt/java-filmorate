package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Get film: {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/search")
    public List<Film> search(@RequestParam String query,
                             @RequestParam String by) {
        log.info("Search films: {}, by: {}", query, by);
        return filmService.search(query, by);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("Create film: {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Update film: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Like film: {}, userID {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Unlike film: {}, userID {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsPopular(@RequestParam(required = false) Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        log.info("Popular films args: count: {}, genreId :{}, year: {}", count, genreId, year);
        return filmService.getListOfPopularFilms(count, genreId, year);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        log.info("Delete film: {}", id);
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.info("Get common films: userId: {}, friendId: {}", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable int directorId,
                                         @RequestParam(defaultValue = "likes") String sortBy) {
        log.info("Get films by director: directorId {}, sortBy {}", directorId, sortBy);
        return filmService.getFilmsByDirector(directorId, sortBy);
    }
}
