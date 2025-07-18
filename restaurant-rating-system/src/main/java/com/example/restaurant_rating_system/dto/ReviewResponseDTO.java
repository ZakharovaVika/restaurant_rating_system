package com.example.restaurant_rating_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "DTO для ответа с данными об отзыве")
public class ReviewResponseDTO {
    @Schema(description = "ID посетителя", example = "1")
    Long visitorId;

    @Schema(description = "ID ресторана", example = "1")
    Long restaurantId;

    @Schema(description = "Оценка", example = "5")
    int rating;

    @Schema(description = "Текст отзыва", example = "Great food and service!")
    String reviewText;
}
