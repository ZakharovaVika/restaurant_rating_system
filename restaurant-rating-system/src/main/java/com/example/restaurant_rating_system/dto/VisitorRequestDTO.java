package com.example.restaurant_rating_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Value;

@Value
@Schema(description = "DTO для создания/обновления посетителя")
public class VisitorRequestDTO {
    @Schema(description = "Имя посетителя", example = "John Doe")
    String name;

    @NotNull(message = "Возраст не может быть null")
    @Min(value = 18, message = "Возраст должен быть не меньше 18")
    @Schema(description = "Возраст посетителя", example = "30")
    int age;

    @NotBlank(message = "Пол не может быть пустым")
    @Schema(description = "Пол посетителя", example = "Male")
    String gender;
}
