package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.exception.DuplicateEmailException;
import com.ems.exception.EmployeeNotFoundException;
import com.ems.model.Employee;
import com.ems.model.Employee.EmployeeStatus;
import com.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Unit Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee sampleEmployee;
    private EmployeeDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@company.com")
                .phone("+1234567890")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .salary(75000.0)
                .hireDate(LocalDate.of(2022, 1, 15))
                .status(EmployeeStatus.ACTIVE)
                .build();

        sampleDTO = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@company.com")
                .phone("+1234567890")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .salary(75000.0)
                .hireDate(LocalDate.of(2022, 1, 15))
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    // ── CREATE ──

    @Test
    @DisplayName("Should create employee successfully")
    void shouldCreateEmployeeSuccessfully() {
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeDTO result = employeeService.createEmployee(sampleDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john.doe@company.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void shouldThrowWhenEmailDuplicate() {
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(sampleEmployee));

        assertThatThrownBy(() -> employeeService.createEmployee(sampleDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already exists");

        verify(employeeRepository, never()).save(any());
    }

    // ── READ ──

    @Test
    @DisplayName("Should return employee by ID")
    void shouldReturnEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when ID not found")
    void shouldThrowWhenEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── UPDATE ──

    @Test
    @DisplayName("Should update employee successfully")
    void shouldUpdateEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        sampleDTO.setJobTitle("Senior Engineer");
        EmployeeDTO result = employeeService.updateEmployee(1L, sampleDTO);

        assertThat(result).isNotNull();
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw when updating with duplicate email")
    void shouldThrowWhenUpdatingWithDuplicateEmail() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        when(employeeRepository.existsByEmailAndIdNot(anyString(), anyLong())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.updateEmployee(1L, sampleDTO))
                .isInstanceOf(DuplicateEmailException.class);
    }

    // ── DELETE ──

    @Test
    @DisplayName("Should delete employee successfully")
    void shouldDeleteEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        doNothing().when(employeeRepository).delete(any(Employee.class));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(sampleEmployee);
    }

    @Test
    @DisplayName("Should throw when deleting non-existent employee")
    void shouldThrowWhenDeletingNonExistent() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                .isInstanceOf(EmployeeNotFoundException.class);

        verify(employeeRepository, never()).delete(any());
    }

    // ── STATUS ──

    @Test
    @DisplayName("Should update employee status")
    void shouldUpdateEmployeeStatus() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(sampleEmployee));
        sampleEmployee.setStatus(EmployeeStatus.INACTIVE);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        EmployeeDTO result = employeeService.updateEmployeeStatus(1L, EmployeeStatus.INACTIVE);

        assertThat(result.getStatus()).isEqualTo(EmployeeStatus.INACTIVE);
    }
}
