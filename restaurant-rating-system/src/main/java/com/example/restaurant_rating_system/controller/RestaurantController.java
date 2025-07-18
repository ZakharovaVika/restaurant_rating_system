package com.example.restaurant_rating_system.controller;
import com.example.restaurant_rating_system.dto.RestaurantRequestDTO;
import com.example.restaurant_rating_system.dto.RestaurantResponseDTO;
import com.example.restaurant_rating_system.service.RestaurantService;
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
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "API для управления ресторанами")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    @Operation(summary = "Получить все рестораны")
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        List<RestaurantResponseDTO> restaurants = restaurantService.findAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ресторан по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ресторан найден"),
            @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    })
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Long id) {
        Optional<RestaurantResponseDTO> restaurant = restaurantService.findRestaurantById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новый ресторан")
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO) {
        RestaurantResponseDTO savedRestaurant = restaurantService.saveRestaurant(restaurantRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий ресторан")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO) {
        Optional<RestaurantResponseDTO> existingRestaurant = restaurantService.findRestaurantById(id);
        if (existingRestaurant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.removeRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
