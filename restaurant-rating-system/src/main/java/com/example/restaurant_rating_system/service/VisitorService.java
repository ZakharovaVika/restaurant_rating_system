package com.example.restaurant_rating_system.service;
import com.example.restaurant_rating_system.dto.VisitorRequestDTO;
import com.example.restaurant_rating_system.dto.VisitorResponseDTO;
import com.example.restaurant_rating_system.model.Visitor;
import com.example.restaurant_rating_system.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Помечаем класс как Spring Service
public class VisitorService {
    private final VisitorRepository visitorRepository; // Внедрение зависимости
    @Autowired
    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public VisitorResponseDTO saveVisitor(VisitorRequestDTO visitorRequestDTO) {
        // Преобразуем DTO в сущность
        Visitor visitor = new Visitor(null, visitorRequestDTO.getName(), visitorRequestDTO.getAge(), visitorRequestDTO.getGender());
        // Сохраняем сущность
        Visitor savedVisitor = visitorRepository.save(visitor);
        // Преобразуем сущность обратно в DTO
        return convertToResponseDTO(savedVisitor);
    }

    public void removeVisitor(Long id) {
        visitorRepository.remove(id);
    }

    public List<VisitorResponseDTO> findAllVisitors() {
        return visitorRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<VisitorResponseDTO> findVisitorById(Long id) {
        return visitorRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    // Метод для преобразования сущности в DTO
    private VisitorResponseDTO convertToResponseDTO(Visitor visitor) {
        return new VisitorResponseDTO(visitor.getId(), visitor.getName(), visitor.getAge(), visitor.getGender());
    }
}
