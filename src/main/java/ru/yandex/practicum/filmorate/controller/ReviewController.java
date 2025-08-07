package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> getReviews(
            @RequestParam(required = false) @Positive Long filmId,
            @RequestParam(required = false) @Positive Integer count) {
        log.info("Get reviews by filmId: {}, count {}", filmId, count);
        return reviewService.getReviews(filmId, count);
    }

    @GetMapping("/{id}")
    public ReviewDTO getOneReview(@PathVariable @Positive Long id) {
        log.info("Get review by id: {}", id);
        return reviewService.getOneReview(id);
    }

    @PostMapping
    public ReviewDTO create(@RequestBody @Valid ReviewCreate review) {
        log.info("Create review: {}", review);
        return reviewService.create(review);
    }

    @PutMapping
    public ReviewDTO update(@RequestBody @Valid ReviewUpdate review) {
        log.info("Update review: {}", review);
        return reviewService.update(review);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {
        log.info("Like review with id: {}, userId: {}", id, userId);
        reviewService.setLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void setDisLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {
        log.info("Dislike review with id: {}, userId: {}", id, userId);
        reviewService.setDisLikeReview(id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        log.info("Delete review with id: {}", id);
        reviewService.delete(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unSetLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {
        log.info("Unlike review with id: {}, userId: {}", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void unSetDisLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {
        log.info("UnSetDislike review with id: {}, userId: {}", id, userId);
        reviewService.deleteDisLike(id, userId);
    }

}
