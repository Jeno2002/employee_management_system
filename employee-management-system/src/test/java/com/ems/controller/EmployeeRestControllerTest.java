package com.ems.controller;

import com.ems.dto.EmployeeDTO;
import com.ems.model.Employee.EmployeeStatus;
import com.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeRestController.class)
@DisplayName("EmployeeRestController Integration Tests")
class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDTO sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = EmployeeDTO.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@company.com")
                .phone("+1987654321")
                .department("HR")
                .jobTitle("HR Manager")
                .salary(65000.0)
                .hireDate(LocalDate.of(2021, 6, 1))
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/employees - returns 200 with paginated list")
    void getAllEmployees_Returns200() throws Exception {
        when(employeeService.getAllEmployees(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleEmployee)));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].firstName").value("Jane"));
    }

    @Test
    @DisplayName("GET /api/v1/employees/{id} - returns 200 with employee")
    void getEmployeeById_Returns200() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleEmployee);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("jane.smith@company.com"));
    }

    @Test
    @DisplayName("POST /api/v1/employees - returns 201 on valid input")
    void createEmployee_Returns201() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(sampleEmployee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    @DisplayName("DELETE /api/v1/employees/{id} - returns 200")
    void deleteEmployee_Returns200() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
