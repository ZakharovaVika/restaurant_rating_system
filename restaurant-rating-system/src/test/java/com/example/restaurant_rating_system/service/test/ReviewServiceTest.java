package com.example.restaurant_rating_system.service.test;
import com.example.restaurant_rating_system.dto.ReviewRequestDTO;
import com.example.restaurant_rating_system.dto.ReviewResponseDTO;
import com.example.restaurant_rating_system.model.Review;
import com.example.restaurant_rating_system.repository.ReviewRepository;
import com.example.restaurant_rating_system.service.RestaurantService;
import com.example.restaurant_rating_system.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private ReviewService reviewService;

    private Review review1;
    private Review review2;

    @BeforeEach
    void setUp() {
        review1 = new Review(1L, 1L, 1L, 5, "Great food!");
        review2 = new Review(2L, 2L, 2L, 4, "Good service.");
    }

    @Test
    void findAllReviews_returnsAllReviews() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review1, review2));

        List<ReviewResponseDTO> reviews = reviewService.findAllReviews();

        assertEquals(2, reviews.size());
        assertEquals(review1.getReviewText(), reviews.get(0).getReviewText());
        assertEquals(review2.getReviewText(), reviews.get(1).getReviewText());
    }

    @Test
    void findReviewById_returnsReview_whenReviewExists() {
        when(reviewRepository.findById(1L, 1L)).thenReturn(Optional.of(review1));

        Optional<ReviewResponseDTO> review = reviewService.findReviewById(1L, 1L);

        assertEquals(review1.getReviewText(), review.get().getReviewText());
    }

    @Test
    void saveReview_returnsSavedReview() {
        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(1L, 1L, 5, "Excellent!");
        Review reviewToSave = new Review(null, reviewRequestDTO.getVisitorId(), reviewRequestDTO.getRestaurantId(), reviewRequestDTO.getRating(), reviewRequestDTO.getReviewText());
        Review savedReview = new Review(3L, reviewRequestDTO.getVisitorId(), reviewRequestDTO.getRestaurantId(), reviewRequestDTO.getRating(), reviewRequestDTO.getReviewText());

        when(reviewRepository.save(reviewToSave)).thenReturn(savedReview);

        ReviewResponseDTO reviewResponseDTO = reviewService.saveReview(reviewRequestDTO);

        assertEquals(savedReview.getId(), reviewResponseDTO.getRestaurantId());
        assertEquals(savedReview.getReviewText(), reviewResponseDTO.getReviewText());
    }
}
