package com.example.restaurant_rating_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long visitorId; // Обязательное поле
    private Long restaurantId; // Обязательное поле
    private int rating; // Оценка (например, от 1 до 5)
    private String reviewText; // Может быть пустым
}
