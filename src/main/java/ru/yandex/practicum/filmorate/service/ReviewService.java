package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewStorage reviewStorage;
    private static final short LIKE_VALUE = 1;
    private static final short DISLIKE_VALUE = -1;
    private final static int DEFAULT_COUNT = 10;

    public ReviewDTO getOneReview(Long id) {
        return ReviewMapper.mapToReviewDTO(
                reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден")));
    }

    public List<ReviewDTO> getReviews(Long filmId, Integer count) {
        List<Review> list = reviewStorage.getMany(filmId);

        return reviewStorage.getMany(filmId).stream()
               .map(review ->  ReviewMapper.mapToReviewDTO(review))
               .limit( count == null ? DEFAULT_COUNT : count )
               .toList();
    }

    public ReviewDTO create(ReviewCreate newReview) {
        User userId = userService.getUserById(newReview.getUserId());
        if (userId == null)
            throw new NotFoundException("Отсутствует фильм с id %d".formatted(newReview.getFilmId()));

        Film filmId = filmService.getFilmById(newReview.getFilmId());
        if (filmId == null)
            throw new NotFoundException("Отсутствует пользователь с id %d".formatted(newReview.getUserId()));

        return ReviewMapper.mapToReviewDTO(reviewStorage.create(ReviewMapper.mapToReview(newReview)));
    }

    public void delete(Long id) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        reviewStorage.delete(id);
    }

    private void checkAndSetLikeValue(Long id, Long userId, short value) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        if (userService.getUserById(userId) == null)
            throw new NotFoundException("Пользователь не найден");
        reviewStorage.setLikeValue(id, userId, value);
    }

    public void setLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, LIKE_VALUE);
    }

    public void setDisLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, DISLIKE_VALUE);
    }

    private void deleteLikeValue(Long id, Long userId, short value) {
        reviewStorage.deleteLikeValue(id, userId, value);
    }

    public void deleteLike(Long id, Long userId) {
        deleteLikeValue(id, userId, LIKE_VALUE);
    }

    public void deleteDisLike(Long id, Long userId) {
        deleteLikeValue(id, userId, DISLIKE_VALUE);
    }

    public ReviewDTO update(ReviewUpdate reviewUpdate) {
      Review reviewOld = reviewStorage
              .get(reviewUpdate.getReviewId()).orElseThrow(() -> new NotFoundException("Отзыв не найден"));
      Review review = ReviewMapper.updateRevievFields(reviewOld, reviewUpdate);
      if (filmService.getFilmById(review.getFilm()) == null)
          throw new NotFoundException("Фильм не найден");
      if (userService.getUserById(review.getUser()) == null)
          throw new NotFoundException("Пользователь не найден");

      return ReviewMapper.mapToReviewDTO(reviewStorage.update(review));
    }

}
