package com.example.restaurant_rating_system.service.test;
import com.example.restaurant_rating_system.dto.RestaurantRequestDTO;
import com.example.restaurant_rating_system.dto.RestaurantResponseDTO;
import com.example.restaurant_rating_system.enums.CuisineType;
import com.example.restaurant_rating_system.model.Restaurant;
import com.example.restaurant_rating_system.repository.RestaurantRepository;
import com.example.restaurant_rating_system.repository.ReviewRepository;
import com.example.restaurant_rating_system.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant(1L, "La Piazza", "Italian", CuisineType.ITALIAN, BigDecimal.valueOf(25.0), BigDecimal.valueOf(4.5));
        restaurant2 = new Restaurant(2L, "Sushi Bar", "Japanese", CuisineType.JAPANESE, BigDecimal.valueOf(30.0), BigDecimal.valueOf(4.0));
    }

    @Test
    void findAllRestaurants_returnsAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        List<RestaurantResponseDTO> restaurants = restaurantService.findAllRestaurants();

        assertEquals(2, restaurants.size());
        assertEquals(restaurant1.getName(), restaurants.get(0).getName());
        assertEquals(restaurant2.getName(), restaurants.get(1).getName());
    }

    @Test
    void findRestaurantById_returnsRestaurant_whenRestaurantExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));

        Optional<RestaurantResponseDTO> restaurant = restaurantService.findRestaurantById(1L);

        assertEquals(restaurant1.getName(), restaurant.get().getName());
    }

    @Test
    void saveRestaurant_returnsSavedRestaurant() {
        RestaurantRequestDTO restaurantRequestDTO = new RestaurantRequestDTO("New Restaurant", "Description", CuisineType.valueOf("ITALIAN"), BigDecimal.valueOf(20.0));
        Restaurant restaurantToSave = new Restaurant(null, restaurantRequestDTO.getName(), restaurantRequestDTO.getDescription(), restaurantRequestDTO.getCuisineType(), restaurantRequestDTO.getAverageCheckPerPerson(), BigDecimal.valueOf(0.0));
        Restaurant savedRestaurant = new Restaurant(3L, restaurantRequestDTO.getName(), restaurantRequestDTO.getDescription(), restaurantRequestDTO.getCuisineType(), restaurantRequestDTO.getAverageCheckPerPerson(), BigDecimal.valueOf(0.0));

        when(restaurantRepository.save(restaurantToSave)).thenReturn(savedRestaurant);

        RestaurantResponseDTO restaurantResponseDTO = restaurantService.saveRestaurant(restaurantRequestDTO);

        assertEquals(savedRestaurant.getId(), restaurantResponseDTO.getId());
        assertEquals(savedRestaurant.getName(), restaurantResponseDTO.getName());
    }
}
