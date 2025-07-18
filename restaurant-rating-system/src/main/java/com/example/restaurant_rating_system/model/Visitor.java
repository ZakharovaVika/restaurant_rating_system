package com.example.restaurant_rating_system.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor // Конструктор без аргументов (для Lombok)
@AllArgsConstructor // Конструктор со всеми аргументами
public class Visitor {
    private Long id; // Обязательное поле
    private String name; // Необязательное поле
    private int age; // Обязательное поле
    private String gender; // Обязательное поле
}
