package com.example.restaurant_rating_system.repository;

import com.example.restaurant_rating_system.model.Restaurant;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RestaurantRepository implements Repository<Restaurant, Long> {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            Long newId = idGenerator.incrementAndGet();
            restaurant.setId(newId);
            restaurants.add(restaurant);
        } else {
            for (int i = 0; i < restaurants.size(); i++) {
                if (restaurants.get(i).getId().equals(restaurant.getId())) {
                    restaurants.set(i, restaurant);
                    break;
                }
            }
        }
        return restaurant;
    }

    @Override
    public void remove(Long id) {
        restaurants.removeIf(restaurant -> restaurant.getId().equals(id));
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurants.stream()
                .filter(restaurant -> restaurant.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }
}
