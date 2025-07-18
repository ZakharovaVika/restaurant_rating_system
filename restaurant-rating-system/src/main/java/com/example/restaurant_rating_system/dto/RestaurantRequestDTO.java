package com.example.restaurant_rating_system.dto;
import com.example.restaurant_rating_system.enums.CuisineType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Schema(description = "DTO для создания/обновления ресторана")

public class RestaurantRequestDTO {
    @NotBlank(message = "Название не может быть пустым")
    @Schema(description = "Название ресторана", example = "La Piazza")
    String name;

    @Schema(description = "Описание ресторана", example = "Authentic Italian cuisine")
    String description;

    @NotNull(message = "Тип кухни не может быть null")
    @Schema(description = "Тип кухни", example = "ITALIAN")
    CuisineType cuisineType;

    @NotNull(message = "Средний чек не может быть null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Средний чек должен быть больше 0")
    @Schema(description = "Средний чек на человека", example = "25.50")
    BigDecimal averageCheckPerPerson;
}
