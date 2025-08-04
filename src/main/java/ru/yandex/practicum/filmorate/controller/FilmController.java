package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/search")
    public List<Film> search(@RequestParam String query,
                             @RequestParam String by) {
        return filmService.search(query, by);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsPopular(@RequestParam(required = false) Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {

        int resultCount;
        if (count != null && count > 0) {
            resultCount = count;
        } else {
            resultCount = Integer.MAX_VALUE;
        }

        return filmService.getListOfPopularFilms(resultCount, genreId, year);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable int directorId,
                                         @RequestParam(defaultValue = "likes") String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }
}
