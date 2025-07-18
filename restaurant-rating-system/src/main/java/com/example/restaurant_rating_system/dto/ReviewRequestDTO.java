package com.example.restaurant_rating_system.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;

@Value
@Schema(description = "DTO для создания отзыва")
public class ReviewRequestDTO {
    @NotNull(message = "ID посетителя не может быть null")
    @Schema(description = "ID посетителя", example = "1")
    Long visitorId;

    @NotNull(message = "ID ресторана не может быть null")
    @Schema(description = "ID ресторана", example = "1")
    Long restaurantId;

    @Min(value = 1, message = "Оценка должна быть не меньше 1")
    @Max(value = 5, message = "Оценка должна быть не больше 5")
    @Schema(description = "Оценка", example = "5")
    int rating;

    @Schema(description = "Текст отзыва", example = "Great food and service!")
    String reviewText;
}
