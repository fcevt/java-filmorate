package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

public class FilmControllerTest {
    final FilmStorage filmStorage = new InMemoryFilmStorage();

    @Test
    public void creatingMovieWithCorrectDateAndNameTest() {
        Film film = new Film();
        film.setName("dfgh");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDescription("sdfdsf");
        film.setDuration(12);
        Film film1 = filmStorage.create(film);
        Assertions.assertEquals(film.getName(), film1.getName());
        Assertions.assertEquals(film.getDescription(), film1.getDescription());
        Assertions.assertEquals(film.getDuration(), film1.getDuration());
        Assertions.assertEquals(film.getReleaseDate(), film1.getReleaseDate());
    }

    @Test
    public void creatingMovieWithEarlyDate() {
        Film film = new Film();
        film.setName("dfgh");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDescription("sdfdsf");
        film.setDuration(12);
        Assertions.assertThrows(ValidationException.class, () -> filmStorage.create(film));
    }


    @Test
    public void creatingMovieWithDescriptionOf200Characters() {
        Film film = new Film();
        film.setName("dfgh");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDescription("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccbbbbbccccccccccccccccccccnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnncccccccccccccccccccccccccccccccccccccccccccc");
        film.setDuration(12);
        Film film1 = filmStorage.create(film);
        Assertions.assertEquals(film.getName(), film1.getName());
        Assertions.assertEquals(film.getDescription(), film1.getDescription());
        Assertions.assertEquals(film.getDuration(), film1.getDuration());
        Assertions.assertEquals(film.getReleaseDate(), film1.getReleaseDate());
    }

    @Test
    public void creatingMovieWithDurationZeroTest() {
        Film film = new Film();
        film.setName("dfgh");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDescription("sdfdsf");
        film.setDuration(0);
        Film film1 = filmStorage.create(film);
        Assertions.assertEquals(film.getName(), film1.getName());
        Assertions.assertEquals(film.getDescription(), film1.getDescription());
        Assertions.assertEquals(film.getDuration(), film1.getDuration());
        Assertions.assertEquals(film.getReleaseDate(), film1.getReleaseDate());
    }

}
