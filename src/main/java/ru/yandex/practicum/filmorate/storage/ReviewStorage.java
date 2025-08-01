package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review create(Review review);

    Optional<Review> get(Long id);

    List<Review> getMany(Long id);

    void delete(Long id);

    void setLikeValue(Long id, Long userId, short value);

    void deleteLikeValue(Long id, Long userId, short value);

    Review update(Review review);
}
