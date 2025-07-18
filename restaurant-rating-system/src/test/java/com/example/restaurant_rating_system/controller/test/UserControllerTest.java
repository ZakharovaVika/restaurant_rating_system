package com.example.restaurant_rating_system.controller.test;
import com.example.restaurant_rating_system.dto.VisitorRequestDTO;
import com.example.restaurant_rating_system.dto.VisitorResponseDTO;
import com.example.restaurant_rating_system.service.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitorService visitorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllVisitors_returnsOk() throws Exception {
        when(visitorService.findAllVisitors()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/visitors"))
                .andExpect(status().isOk());
    }

    @Test
    void getVisitorById_returnsOk_whenVisitorExists() throws Exception {
        VisitorResponseDTO visitor = new VisitorResponseDTO(1L, "John Doe", 30, "Male");
        when(visitorService.findVisitorById(1L)).thenReturn(Optional.of(visitor));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/visitors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void createVisitor_returnsCreated() throws Exception {
        VisitorRequestDTO visitorRequestDTO = new VisitorRequestDTO("Test User", 25, "Other");
        VisitorResponseDTO savedVisitor = new VisitorResponseDTO(1L, "Test User", 25, "Other");
        when(visitorService.saveVisitor(any(VisitorRequestDTO.class))).thenReturn(savedVisitor);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void deleteVisitor_returnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/visitors/1"))
                .andExpect(status().isNoContent());
    }
}
