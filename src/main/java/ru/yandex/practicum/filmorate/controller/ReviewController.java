package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.dto.review.ReviewUpdate;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDTO> getReviews(
            @RequestParam(required = false) @Positive Long filmId,
            @RequestParam(required = false) @Positive Long count) {
        return null;
    }

    @GetMapping("/{id}")
    public ReviewDTO getOneReview(@PathVariable @Positive Long id) {
        return reviewService.getOneReview(id);
    }

    @PostMapping
    public ReviewDTO create(@RequestBody @Valid ReviewCreate review) {
        return reviewService.create(review);
    }

    @PutMapping
    public ReviewDTO update(@RequestBody ReviewUpdate review) {
        return null;
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {

    }

    @PutMapping("/{id}/dislike/{userId}")
    public void setDisLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {

    }

    @DeleteMapping
    public void delete(@PathVariable @Positive Long id) {

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unSetLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {

    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void unSetDisLikeReview(
            @PathVariable @Positive Long id,
            @PathVariable @Positive Long userId) {

    }

}
