package com.example.restaurant_rating_system.controller;
import com.example.restaurant_rating_system.dto.ReviewRequestDTO;
import com.example.restaurant_rating_system.dto.ReviewResponseDTO;
import com.example.restaurant_rating_system.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "API для управления отзывами")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewService.findAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Получить отзыв по ID посетителя и ID ресторана")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв найден"),
            @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    })
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        Optional<ReviewResponseDTO> review = reviewService.findReviewById(visitorId, restaurantId);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        ReviewResponseDTO savedReview = reviewService.saveReview(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> deleteReview(@PathVariable Long visitorId, @PathVariable Long restaurantId) {
        reviewService.removeReview(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
}
