package com.example.restaurant_rating_system.service;
import com.example.restaurant_rating_system.model.Restaurant;
import com.example.restaurant_rating_system.repository.RestaurantRepository;
import com.example.restaurant_rating_system.repository.ReviewRepository; // Нужен для пересчета оценки
import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.enums.CuisineType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        // Проверка обязательных полей
        if (restaurant.getName() == null || restaurant.getName().isEmpty() ||
                restaurant.getCuisineType() == null || restaurant.getAverageCheckPerPerson() == null ||
                restaurant.getUserRating() == null) {
            throw new IllegalArgumentException("Name, CuisineType, AverageCheckPerPerson, and UserRating are mandatory for a restaurant.");
        }
        // Для новых ресторанов оценка должна быть 0.0
        if (restaurant.getId() == null && restaurant.getUserRating().compareTo(BigDecimal.ZERO) != 0) {
            restaurant.setUserRating(BigDecimal.ZERO);
        }
        return restaurantRepository.save(restaurant);
    }

    public void removeRestaurant(Long id) {
        // Перед удалением ресторана, возможно, нужно удалить все его отзывы
        reviewRepository.findByRestaurantId(id).forEach(review ->
                reviewRepository.remove(review.getVisitorId(), review.getRestaurantId())
        );
        restaurantRepository.remove(id);
    }

    public List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    /**
     * Пересчитывает среднюю оценку ресторана на основе всех его отзывов.
     * @param restaurantId ID ресторана, для которого нужно пересчитать оценку.
     * @return Обновленный объект Restaurant или Optional.empty() если ресторан не найден.
     */
    public Optional<Restaurant> recalculateRestaurantRating(Long restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            List<Review> reviewsForRestaurant = reviewRepository.findByRestaurantId(restaurantId);

            if (reviewsForRestaurant.isEmpty()) {
                // Если отзывов нет, устанавливаем оценку в 0
                restaurant.setUserRating(BigDecimal.ZERO);
            } else {
                // Считаем среднюю оценку
                // Сначала суммируем все оценки
                int sumOfRatings = reviewsForRestaurant.stream()
                        .mapToInt(Review::getRating)
                        .sum();
                // Считаем среднее
                BigDecimal averageRating = BigDecimal.valueOf(sumOfRatings)
                        .divide(BigDecimal.valueOf(reviewsForRestaurant.size()), 2, RoundingMode.HALF_UP);
                restaurant.setUserRating(averageRating);
            }
            // Сохраняем обновленный ресторан
            return Optional.of(restaurantRepository.save(restaurant));
        }
        return Optional.empty();
    }
}
