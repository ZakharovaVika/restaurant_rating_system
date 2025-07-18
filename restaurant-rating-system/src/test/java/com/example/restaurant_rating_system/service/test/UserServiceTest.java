package com.example.restaurant_rating_system.service.test;
import com.example.restaurant_rating_system.dto.VisitorRequestDTO;
import com.example.restaurant_rating_system.dto.VisitorResponseDTO;
import com.example.restaurant_rating_system.model.Visitor;
import com.example.restaurant_rating_system.repository.VisitorRepository;
import com.example.restaurant_rating_system.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor1;
    private Visitor visitor2;

    @BeforeEach
    void setUp() {
        visitor1 = new Visitor(1L, "John Doe", 30, "Male");
        visitor2 = new Visitor(2L, "Jane Smith", 25, "Female");
    }

    @Test
    void findAllVisitors_returnsAllVisitors() {
        when(visitorRepository.findAll()).thenReturn(Arrays.asList(visitor1, visitor2));

        List<VisitorResponseDTO> visitors = visitorService.findAllVisitors();

        assertEquals(2, visitors.size());
        assertEquals(visitor1.getName(), visitors.get(0).getName());
        assertEquals(visitor2.getName(), visitors.get(1).getName());
    }

    @Test
    void findVisitorById_returnsVisitor_whenVisitorExists() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor1));

        Optional<VisitorResponseDTO> visitor = visitorService.findVisitorById(1L);

        assertEquals(visitor1.getName(), visitor.get().getName());
    }

    @Test
    void saveVisitor_returnsSavedVisitor() {
        VisitorRequestDTO visitorRequestDTO = new VisitorRequestDTO("Test User", 28, "Other");
        Visitor visitorToSave = new Visitor(null, visitorRequestDTO.getName(), visitorRequestDTO.getAge(), visitorRequestDTO.getGender());
        Visitor savedVisitor = new Visitor(3L, visitorRequestDTO.getName(), visitorRequestDTO.getAge(), visitorRequestDTO.getGender());

        when(visitorRepository.save(visitorToSave)).thenReturn(savedVisitor);

        VisitorResponseDTO visitorResponseDTO = visitorService.saveVisitor(visitorRequestDTO);

        assertEquals(savedVisitor.getId(), visitorResponseDTO.getId());
        assertEquals(savedVisitor.getName(), visitorResponseDTO.getName());
    }
}
