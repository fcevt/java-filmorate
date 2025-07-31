package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class FilmServiceTest {
    UserStorage userStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);
    UserService userService = new UserService(userStorage);


    @Test
    public void likeTest() {
        User user = new User();
        userService.createUser(user);
        Film film = new Film();
        filmService.createFilm(film);
        filmService.addLike(film.getId(), user.getId());
        Assertions.assertEquals(1, filmService.getFilms().getFirst().getLikes().size());
    }

    @Test
    public void dislikeTest() {
        User user = new User();
        userService.createUser(user);
        Film film = new Film();
        filmService.createFilm(film);
        filmService.addLike(film.getId(), user.getId());
        filmService.deleteLike(film.getId(), user.getId());
        Assertions.assertEquals(0, filmService.getFilms().getFirst().getLikes().size());
    }

    @Test
    public void getPopularFilmsTest() {
        User user = new User();
        User user1 = new User();
        userService.createUser(user);
        userService.createUser(user1);
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");
        Film film = new Film();
        film.setGenres(Set.of(genre));
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        filmService.createFilm(film);
        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(film.getId(), user1.getId());
        Film film2 = new Film();
        film2.setGenres(Set.of(genre));
        film2.setReleaseDate(LocalDate.of(2025, 1, 1));
        filmService.createFilm(film2);
        List<Film> popularFilms = filmService.getListOfPopularFilms(2, 1, 2025);
        Assertions.assertEquals(2, popularFilms.size());
        Assertions.assertEquals(film.getId(), popularFilms.get(0).getId());
    }
}
