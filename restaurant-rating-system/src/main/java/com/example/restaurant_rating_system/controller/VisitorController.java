package com.example.restaurant_rating_system.controller;
import com.example.restaurant_rating_system.dto.VisitorRequestDTO;
import com.example.restaurant_rating_system.dto.VisitorResponseDTO;
import com.example.restaurant_rating_system.service.VisitorService;
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
@RequestMapping("/api/visitors")
@Tag(name = "Visitors", description = "API для управления посетителями")
public class VisitorController {
    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    @Operation(summary = "Получить всех посетителей")
    public ResponseEntity<List<VisitorResponseDTO>> getAllVisitors() {
        List<VisitorResponseDTO> visitors = visitorService.findAllVisitors();
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить посетителя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Посетитель найден"),
            @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    })
    public ResponseEntity<VisitorResponseDTO> getVisitorById(@PathVariable Long id) {
        Optional<VisitorResponseDTO> visitor = visitorService.findVisitorById(id);
        return visitor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать нового посетителя")
    public ResponseEntity<VisitorResponseDTO> createVisitor(@Valid @RequestBody VisitorRequestDTO visitorRequestDTO) {
        VisitorResponseDTO savedVisitor = visitorService.saveVisitor(visitorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVisitor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующего посетителя")
    public ResponseEntity<VisitorResponseDTO> updateVisitor(@PathVariable Long id, @Valid @RequestBody VisitorRequestDTO visitorRequestDTO) {
        Optional<VisitorResponseDTO> existingVisitor = visitorService.findVisitorById(id);
        if (existingVisitor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Здесь нужно обновить существующую сущность и сохранить её
        // (в данном примере это опущено для краткости)
        //VisitorResponseDTO updatedVisitor = visitorService.updateVisitor(id, visitorRequestDTO);
        //return ResponseEntity.ok(updatedVisitor);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        visitorService.removeVisitor(id);
        return ResponseEntity.noContent().build();
    }
}
