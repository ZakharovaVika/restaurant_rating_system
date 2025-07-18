package com.example.restaurant_rating_system.repository;

import com.example.restaurant_rating_system.model.Review;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();

    public Review save(Review review) {
        reviews.add(review);
        return review;
    }
    public void remove(Long visitorId, Long restaurantId) {
        // Удаляем по паре ID
        reviews.removeIf(review -> review.getVisitorId().equals(visitorId) && review.getRestaurantId().equals(restaurantId));
    }
    public Optional<Review> findById(Long visitorId, Long restaurantId) {
        // Ищем по паре ID
        return reviews.stream()
                .filter(review -> review.getVisitorId().equals(visitorId) && review.getRestaurantId().equals(restaurantId))
                .findFirst();
    }

    // Дополнительный метод для поиска всех отзывов по ресторану
    public List<Review> findByRestaurantId(Long restaurantId) {
        return reviews.stream()
                .filter(review -> review.getRestaurantId().equals(restaurantId))
                .toList(); // Используем toList() для Java 16+, или .collect(Collectors.toList())
    }

    // Дополнительный метод для поиска всех отзывов по посетителю
    public List<Review> findByVisitorId(Long visitorId) {
        return reviews.stream()
                .filter(review -> review.getVisitorId().equals(visitorId))
                .toList();
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviews);
    }
}
