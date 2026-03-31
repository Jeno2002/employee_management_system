package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.model.Employee;
import com.ems.model.Employee.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    // CRUD Operations
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);

    // List / Search / Filter
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
    Page<EmployeeDTO> searchEmployees(String query, Pageable pageable);
    Page<EmployeeDTO> getEmployeesByDepartment(String department, Pageable pageable);
    Page<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status, Pageable pageable);

    // Status update
    EmployeeDTO updateEmployeeStatus(Long id, EmployeeStatus status);

    // Statistics for dashboard
    long getTotalEmployees();
    long getActiveEmployees();
    Map<String, Long> getEmployeeCountByDepartment();
    List<String> getAllDepartments();
}
