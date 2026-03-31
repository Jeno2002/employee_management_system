package com.ems.controller;

import com.ems.dto.ApiResponse;
import com.ems.dto.EmployeeDTO;
import com.ems.model.Employee.EmployeeStatus;
import com.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST API Controller
 * Base URL: /api/v1/employees
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeRestController {

    private final EmployeeService employeeService;

    // ──────────────────────────── GET ALL ────────────────────────────

    /**
     * GET /api/v1/employees
     * Returns paginated list of all employees
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeDTO>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeDTO> employees = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }

    // ──────────────────────────── GET BY ID ────────────────────────────

    /**
     * GET /api/v1/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee found", employee));
    }

    // ──────────────────────────── SEARCH ────────────────────────────

    /**
     * GET /api/v1/employees/search?query=...
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<EmployeeDTO>>> searchEmployees(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> results = employeeService.searchEmployees(query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results", results));
    }

    // ──────────────────────────── CREATE ────────────────────────────

    /**
     * POST /api/v1/employees
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO created = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    // ──────────────────────────── UPDATE ────────────────────────────

    /**
     * PUT /api/v1/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    // ──────────────────────────── UPDATE STATUS ────────────────────────────

    /**
     * PATCH /api/v1/employees/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam EmployeeStatus status) {
        EmployeeDTO updated = employeeService.updateEmployeeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Employee status updated", updated));
    }

    // ──────────────────────────── DELETE ────────────────────────────

    /**
     * DELETE /api/v1/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }

    // ──────────────────────────── STATS ────────────────────────────

    /**
     * GET /api/v1/employees/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = Map.of(
                "totalEmployees", employeeService.getTotalEmployees(),
                "activeEmployees", employeeService.getActiveEmployees(),
                "departments", employeeService.getEmployeeCountByDepartment()
        );
        return ResponseEntity.ok(ApiResponse.success("Statistics retrieved", stats));
    }

    /**
     * GET /api/v1/employees/departments
     */
    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<String>>> getDepartments() {
        return ResponseEntity.ok(
                ApiResponse.success("Departments retrieved", employeeService.getAllDepartments())
        );
    }
}
