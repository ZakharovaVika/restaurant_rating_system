package com.example.restaurant_rating_system.repository;

import com.example.restaurant_rating_system.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.restaurantId = :restaurantId")
    List<Review> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT r FROM Review r WHERE r.visitorId = :visitorId AND r.restaurantId = :restaurantId")
    Optional<Review> findById(@Param("visitorId") Long visitorId, @Param("restaurantId") Long restaurantId);

    @Query("DELETE FROM Review r WHERE r.visitorId = :visitorId AND r.restaurantId = :restaurantId")
    void remove(@Param("visitorId") Long visitorId, @Param("restaurantId") Long restaurantId);
}