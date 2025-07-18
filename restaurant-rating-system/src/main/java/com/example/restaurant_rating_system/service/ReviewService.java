package com.example.restaurant_rating_system.service;
import com.example.restaurant_rating_system.dto.ReviewRequestDTO;
import com.example.restaurant_rating_system.dto.ReviewResponseDTO;
import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.repository.ReviewRepository;
import com.example.restaurant_rating_system.repository.RestaurantRepository;
import com.example.restaurant_rating_system.repository.VisitorRepository;
import java.util.stream.Collectors;
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

    public ReviewResponseDTO saveReview(ReviewRequestDTO reviewRequestDTO) {
        Review review = new Review(null, reviewRequestDTO.getVisitorId(), reviewRequestDTO.getRestaurantId(), reviewRequestDTO.getRating(), reviewRequestDTO.getReviewText());
        Review savedReview = reviewRepository.save(review);
        restaurantService.recalculateRestaurantRating(reviewRequestDTO.getRestaurantId()); // Теперь работает!
        return convertToResponseDTO(savedReview);
    }

    public void removeReview(Long visitorId, Long restaurantId) {
        Optional<Review> reviewOptional = reviewRepository.findById(visitorId, restaurantId);
        reviewOptional.ifPresent(review -> {
            reviewRepository.delete(review);
            restaurantService.recalculateRestaurantRating(restaurantId);
        });
    }

    public Optional<ReviewResponseDTO> findReviewById(Long visitorId, Long restaurantId) {
        return reviewRepository.findById(visitorId, restaurantId)
                .map(this::convertToResponseDTO);
    }

    public List<ReviewResponseDTO> findAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ReviewResponseDTO convertToResponseDTO(Review review) {
        return new ReviewResponseDTO(review.getVisitorId(), review.getRestaurantId(), review.getRating(), review.getReviewText());
    }
}
