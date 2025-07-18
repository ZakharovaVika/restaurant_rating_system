package com.example.restaurant_rating_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visitor_id")
    private Long visitorId; // Обязательное поле

    @Column(name = "restaurant_id")
    private Long restaurantId; // Обязательное поле

    private int rating; // Оценка (например, от 1 до 5)
    private String reviewText; // Может быть пустым
}
