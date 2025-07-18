package com.example.restaurant_rating_system.repository;

import com.example.restaurant_rating_system.model.Visitor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong; // Для генерации ID

@Component // Помечаем класс как Spring Bean
public class VisitorRepository implements Repository<Visitor, Long> {
    private final List<Visitor> visitors = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0); // Для автоинкремента ID

    @Override
    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            // Новый посетитель
            Long newId = idGenerator.incrementAndGet();
            visitor.setId(newId);
            visitors.add(visitor);
        } else {
            // Обновление существующего посетителя
            for (int i = 0; i < visitors.size(); i++) {
                if (visitors.get(i).getId().equals(visitor.getId())) {
                    visitors.set(i, visitor);
                    break;
                }
            }
        }
        return visitor;
    }

    @Override
    public void remove(Long id) {
        visitors.removeIf(visitor -> visitor.getId().equals(id));
    }

    @Override
    public Optional<Visitor> findById(Long id) {
        return visitors.stream()
                .filter(visitor -> visitor.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Visitor> findAll() {
        return new ArrayList<>(visitors); // Возвращаем копию списка, чтобы избежать внешних модификаций
    }
}
