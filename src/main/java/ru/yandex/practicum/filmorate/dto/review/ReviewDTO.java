package ru.yandex.practicum.filmorate.dto.review;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewDTO {
    private Long reviewId;
    private String content;
    private boolean isPositive;
    private Long userId;
    private Long filmId;
}
