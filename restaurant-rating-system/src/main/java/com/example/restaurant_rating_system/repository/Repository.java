package com.example.restaurant_rating_system.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    void remove(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}
