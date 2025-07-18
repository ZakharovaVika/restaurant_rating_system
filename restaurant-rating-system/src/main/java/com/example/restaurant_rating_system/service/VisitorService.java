package com.example.restaurant_rating_system.service;
import com.example.restaurant_rating_system.model.Visitor;
import com.example.restaurant_rating_system.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Помечаем класс как Spring Service
public class VisitorService {
    private final VisitorRepository visitorRepository; // Внедрение зависимости
    @Autowired
    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor saveVisitor(Visitor visitor) {
        // Здесь могла бы быть бизнес-логика: валидация, проверка уникальности имени и т.д.
        // Для простоты, просто сохраняем.
        // Проверка на null обязательных полей:
        if (visitor.getAge() <= 0 || visitor.getGender() == null || visitor.getGender().isEmpty()) {
            throw new IllegalArgumentException("Age and Gender are mandatory for a visitor.");
        }
        return visitorRepository.save(visitor);
    }

    public void removeVisitor(Long id) {
        // Здесь могла бы быть логика: например, сначала удалить все отзывы этого посетителя.
        // В данном случае, мы просто удаляем посетителя.
        visitorRepository.remove(id);
    }

    public List<Visitor> findAllVisitors() {
        return visitorRepository.findAll();
    }

    public Optional<Visitor> findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }
}
