package com.example.restaurant_rating_system.controller.test;
import com.example.restaurant_rating_system.dto.RestaurantRequestDTO;
import com.example.restaurant_rating_system.dto.RestaurantResponseDTO;
import com.example.restaurant_rating_system.enums.CuisineType;
import com.example.restaurant_rating_system.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRestaurants_returnsOk() throws Exception {
        when(restaurantService.findAllRestaurants()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants"))
                .andExpect(status().isOk());
    }

    @Test
    void getRestaurantById_returnsOk_whenRestaurantExists() throws Exception {
        RestaurantResponseDTO restaurant = new RestaurantResponseDTO(1L, "La Piazza", "Italian", CuisineType.ITALIAN, BigDecimal.valueOf(25.0), BigDecimal.valueOf(4.5));
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.of(restaurant));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("La Piazza"));
    }

    @Test
    void createRestaurant_returnsCreated() throws Exception {
        RestaurantRequestDTO restaurantRequestDTO = new RestaurantRequestDTO("New Restaurant", "Description", CuisineType.valueOf("ITALIAN"), BigDecimal.valueOf(20.0));
        RestaurantResponseDTO savedRestaurant = new RestaurantResponseDTO(1L, "New Restaurant", "Description", CuisineType.ITALIAN, BigDecimal.valueOf(20.0), BigDecimal.valueOf(0.0));
        when(restaurantService.saveRestaurant(any(RestaurantRequestDTO.class))).thenReturn(savedRestaurant);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Restaurant"));
    }

    @Test
    void deleteRestaurant_returnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());
    }
}
