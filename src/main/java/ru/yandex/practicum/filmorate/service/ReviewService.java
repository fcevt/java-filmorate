package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final FilmService filmService;
    private final ReviewStorage reviewStorage;
    private static final short LIKE_VALUE = 1;
    private static final short DISLIKE_VALUE = -1;
    private final int limit = 10;

    public ReviewDTO getOneReview(Long id) {
        return ReviewMapper.mapToReviewDTO(
                reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден")));
    }

    public List<ReviewDTO> getReviews(Long filmId, Integer count) {
        return reviewStorage.getMany(filmId).stream()
                .map(ReviewMapper::mapToReviewDTO)
                .limit(count == null ? limit : count)
                .toList();
    }

    public ReviewDTO create(ReviewCreate newReview) {
        // проверки
        userService.getUserById(newReview.getUserId());
        filmService.getFilmById(newReview.getFilmId());
        return ReviewMapper.mapToReviewDTO(reviewStorage.create(ReviewMapper.mapToReview(newReview)));
    }

    public void delete(Long id) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        reviewStorage.delete(id);
    }

    private void checkAndSetLikeValue(Long id, Long userId, short value) {
        reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Отзыв не найден"));
        // проверка
        userService.getUserById(userId);

        if (!reviewStorage.findExistLikeForReviewUser(id, userId))
            reviewStorage.setLikeValue(id, userId, value);
       else
            reviewStorage.updateLikeValue(id, userId, value);
    }

    public void setLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, LIKE_VALUE);
    }

    public void setDisLikeReview(Long id, Long userId) {
        checkAndSetLikeValue(id, userId, DISLIKE_VALUE);
    }

    private void deleteLikeValue(Long id, Long userId, short value) {
        if (reviewStorage.findExistLikeForReviewUserValue(id, userId, value))
            throw new NotFoundException("Нечего удалять");
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
        Review review = ReviewMapper.updateReviewFields(reviewOld, reviewUpdate);
        // проверки
        filmService.getFilmById(review.getFilm());
        userService.getUserById(review.getUser());
        return ReviewMapper.mapToReviewDTO(reviewStorage.update(review));
    }

}
