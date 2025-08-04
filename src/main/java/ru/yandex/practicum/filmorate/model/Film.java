package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.annotation.MinDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    @MinDate(value = "1895-12-28", message = "Дата релиза должна быть позже 28.12.1895")
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность должна быт положительным числом")
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private Set<Long> likes = new HashSet<>();
    @EqualsAndHashCode.Exclude
    private MPA mpa;
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres;
    @EqualsAndHashCode.Exclude
    private Set<Director> directors;

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasMpa() {
        return mpa.getId() != null;
    }

    public boolean hasGenres() {
        return genres != null; //&& !genres.isEmpty();
    }

    public boolean hasLikes() {
        return likes != null && !likes.isEmpty();
    }

    public boolean hasDirectors() {
        return directors != null && !directors.isEmpty();
    }
}
