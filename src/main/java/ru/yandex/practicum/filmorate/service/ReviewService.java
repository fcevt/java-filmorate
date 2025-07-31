package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.ReviewCreate;
import ru.yandex.practicum.filmorate.dto.review.ReviewDTO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
  private final UserService userService;
  private final FilmService filmService;
  private final ReviewStorage reviewStorage;

  public ReviewDTO getOneReview(Long id) {
      Optional<Review> review = reviewStorage.get(id);
      return ReviewMapper.mapToReviewDTO(
              reviewStorage.get(id).orElseThrow(() -> new NotFoundException("Фильм не найден")));
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
}
