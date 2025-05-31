package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class FilmComparatorByLikes implements Comparator<Film> {

    @Override
    public int compare(Film f1, Film f2) {
        if (f1.getLikes().size() > f2.getLikes().size()) {
            return 1;
        } else if (f1.getLikes().size() < f2.getLikes().size()) {
            return -1;
        } else {
            return 0;
        }
    }
}
