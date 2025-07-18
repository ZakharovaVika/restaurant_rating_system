package com.example.restaurant_rating_system.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "DTO для ответа с данными о посетителе")

public class VisitorResponseDTO {
    @Schema(description = "ID посетителя", example = "1")
    Long id;

    @Schema(description = "Имя посетителя", example = "John Doe")
    String name;

    @Schema(description = "Возраст посетителя", example = "30")
    int age;

    @Schema(description = "Пол посетителя", example = "Male")
    String gender;
}
