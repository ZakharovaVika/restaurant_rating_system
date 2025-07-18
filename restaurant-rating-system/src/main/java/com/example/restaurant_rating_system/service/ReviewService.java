package com.example.restaurant_rating_system.service;

import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.repository.ReviewRepository;
import com.example.restaurant_rating_system.repository.RestaurantRepository;
import com.example.restaurant_rating_system.repository.VisitorRepository;
import com.example.restaurant_rating_system.model.Restaurant;
import com.example.restaurant_rating_system.model.Visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final RestaurantService restaurantService; // Для пересчета оценки

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         RestaurantRepository restaurantRepository,
                         VisitorRepository visitorRepository,
                         RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
        this.restaurantService = restaurantService;
    }

    public Review saveReview(Review review) {
        // Валидация:
        // 1. Проверить, что посетитель и ресторан существуют
        Optional<Visitor> visitor = visitorRepository.findById(review.getVisitorId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(review.getRestaurantId());

        if (!visitor.isPresent()) {
            throw new IllegalArgumentException("Visitor with ID " + review.getVisitorId() + " not found.");
        }
        if (!restaurant.isPresent()) {
            throw new IllegalArgumentException("Restaurant with ID " + review.getRestaurantId() + " not found.");
        }

        // 2. Проверить, что оценка в допустимом диапазоне (например, 1-5)
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // 3. Проверить, что такой отзыв уже есть (опционально, для уникальности)
        Optional<Review> existingReview = reviewRepository.findById(review.getVisitorId(), review.getRestaurantId());
        if (existingReview.isPresent()) {

            reviewRepository.remove(review.getVisitorId(), review.getRestaurantId());
        }

        Review savedReview = reviewRepository.save(review); // Сохраняем новый отзыв

        // 4. Пересчитать среднюю оценку ресторана
        restaurantService.recalculateRestaurantRating(review.getRestaurantId());

        return savedReview;
    }

    public void removeReview(Long visitorId, Long restaurantId) {
        // Удаляем отзыв
        reviewRepository.remove(visitorId, restaurantId);

        // Пересчитываем оценку ресторана после удаления отзыва
        // (если ресторан все еще существует)
        restaurantRepository.findById(restaurantId).ifPresent(restaurant ->
                restaurantService.recalculateRestaurantRating(restaurantId)
        );
    }

    public Optional<Review> findReviewById(Long visitorId, Long restaurantId) {
        return reviewRepository.findById(visitorId, restaurantId);
    }

    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> findReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    public List<Review> findReviewsByVisitorId(Long visitorId) {
        return reviewRepository.findByVisitorId(visitorId);
    }
}
