package com.example.restaurant_rating_system.dto;

import com.example.restaurant_rating_system.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "DTO для ответа с данными о ресторане")
public class RestaurantResponseDTO {
    @Schema(description = "ID ресторана", example = "1")
    Long id;

    @Schema(description = "Название ресторана", example = "La Piazza")
    String name;

    @Schema(description = "Описание ресторана", example = "Authentic Italian cuisine")
    String description;

    @Schema(description = "Тип кухни", example = "ITALIAN")
    CuisineType cuisineType;

    @Schema(description = "Средний чек на человека", example = "25.50")
    BigDecimal averageCheckPerPerson;

    @Schema(description = "Оценка пользователей", example = "4.5")
    BigDecimal userRating;
}
