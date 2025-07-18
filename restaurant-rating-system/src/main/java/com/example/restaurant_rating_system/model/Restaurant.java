package com.example.restaurant_rating_system.model;

import com.example.restaurant_rating_system.enums.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id; // Обязательное поле
    private String name; // Обязательное поле
    private String description; // Может быть пустым
    private CuisineType cuisineType; // Обязательное поле
    private BigDecimal averageCheckPerPerson; // Обязательное поле
    private BigDecimal userRating; // Обязательное поле (например, 0.0 для нового ресторана)

}
