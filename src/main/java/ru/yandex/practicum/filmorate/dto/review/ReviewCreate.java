package ru.yandex.practicum.filmorate.dto.review;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ReviewCreate {
  @NotBlank
  private String content;
  private boolean isPositive;
  @Min(value = 1)
  private long userId;
  @Min(value = 1)
  private long filmId;
}
