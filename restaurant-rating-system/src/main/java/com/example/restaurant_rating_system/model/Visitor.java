package com.example.restaurant_rating_system.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "visitors")
@Data
@NoArgsConstructor // Конструктор без аргументов (для Lombok)
@AllArgsConstructor // Конструктор со всеми аргументами
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Обязательное поле

    private String name; // Необязательное поле
    private int age; // Обязательное поле
    private String gender; // Обязательное поле
}
