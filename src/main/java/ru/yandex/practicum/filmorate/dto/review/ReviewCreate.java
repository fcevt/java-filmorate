package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

@Data
public class ReviewCreate {
  @NotBlank
  private String content;
  @JsonProperty("isPositive")
  private Boolean isPositive;
  @NonNull
  private Long userId;
  @NonNull
  private Long filmId;
}
