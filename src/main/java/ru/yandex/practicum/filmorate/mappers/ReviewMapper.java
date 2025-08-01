package ru.yandex.practicum.filmorate.mappers;


import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.model.Review;

public class ReviewMapper {
    public static Review mapToReview(ReviewCreate newReview) {
        return Review.builder()
                .content(newReview.getContent())
                .isPositive(newReview.getIsPositive())
                .user(newReview.getUserId())
                .film(newReview.getFilmId()).build();
    }

    public static ReviewDTO mapToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.isPositive())
                .userId(review.getUser())
                .filmId(review.getFilm())
                .useful(review.getUseful())
                .build();
    }

    public static Review updateRevievFields(Review review, ReviewUpdate reviewUpdate) {
        if (reviewUpdate.hasContent())
            review.setContent(reviewUpdate.getContent());

//        if (reviewUpdate.hasUserId())
//            review.setUser(reviewUpdate.getUserId());
//
//        if (reviewUpdate.hasFilmId())
//            review.setFilm(reviewUpdate.getFilmId());

        if (reviewUpdate.hasPositive())
            review.setPositive(reviewUpdate.getIsPositive());

        return review;
    }

}
