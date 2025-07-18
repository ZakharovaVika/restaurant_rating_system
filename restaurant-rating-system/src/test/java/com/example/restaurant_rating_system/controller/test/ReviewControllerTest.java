package com.example.restaurant_rating_system.controller.test;
import com.example.restaurant_rating_system.dto.ReviewRequestDTO;
import com.example.restaurant_rating_system.dto.ReviewResponseDTO;
import com.example.restaurant_rating_system.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllReviews_returnsOk() throws Exception {
        when(reviewService.findAllReviews()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    void getReviewById_returnsOk_whenReviewExists() throws Exception {
        ReviewResponseDTO review = new ReviewResponseDTO(1L, 1L, 5, "Excellent!");
        when(reviewService.findReviewById(1L, 1L)).thenReturn(Optional.of(review));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewText").value("Excellent!"));
    }

    @Test
    void createReview_returnsCreated() throws Exception {
        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(1L, 1L, 5, "Excellent!");
        ReviewResponseDTO savedReview = new ReviewResponseDTO(1L, 1L, 5, "Excellent!");
        when(reviewService.saveReview(any(ReviewRequestDTO.class))).thenReturn(savedReview);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewText").value("Excellent!"));
    }

    @Test
    void deleteReview_returnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reviews/1/1"))
                .andExpect(status().isNoContent());
    }
}
