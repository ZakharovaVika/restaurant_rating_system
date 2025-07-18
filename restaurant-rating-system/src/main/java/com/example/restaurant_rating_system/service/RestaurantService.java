package com.example.restaurant_rating_system.service;
import com.example.restaurant_rating_system.dto.RestaurantRequestDTO;
import com.example.restaurant_rating_system.dto.RestaurantResponseDTO;
import com.example.restaurant_rating_system.model.Restaurant;
import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.repository.RestaurantRepository;
import com.example.restaurant_rating_system.repository.ReviewRepository; // Нужен для пересчета оценки
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    public RestaurantResponseDTO saveRestaurant(RestaurantRequestDTO restaurantRequestDTO) {
        Restaurant restaurant = new Restaurant(null, restaurantRequestDTO.getName(), restaurantRequestDTO.getDescription(), restaurantRequestDTO.getCuisineType(), restaurantRequestDTO.getAverageCheckPerPerson(), BigDecimal.ZERO);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToResponseDTO(savedRestaurant);
    }

    public void removeRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDTO> findAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<RestaurantResponseDTO> findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    private RestaurantResponseDTO convertToResponseDTO(Restaurant restaurant) {
        return new RestaurantResponseDTO(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), restaurant.getCuisineType(), restaurant.getAverageCheckPerPerson(), restaurant.getUserRating());
    }

    public void recalculateRestaurantRating(Long restaurantId) {
        // Получаем все отзывы для данного ресторана
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        // Если отзывов нет, устанавливаем рейтинг в 0
        if (reviews.isEmpty()) {
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
            if (restaurant != null) {
                restaurant.setUserRating(BigDecimal.ZERO);
                restaurantRepository.save(restaurant);
            }
            return;
        }

        // Вычисляем средний рейтинг
        BigDecimal totalRating = BigDecimal.ZERO;
        for (Review review : reviews) {
            totalRating = totalRating.add(BigDecimal.valueOf(review.getRating()));
        }
        BigDecimal averageRating = totalRating.divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);

        // Обновляем рейтинг ресторана в базе данных
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            restaurant.setUserRating(averageRating);
            restaurantRepository.save(restaurant);
        }
    }
}
