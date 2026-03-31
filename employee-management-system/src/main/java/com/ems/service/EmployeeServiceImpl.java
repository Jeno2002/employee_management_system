package com.ems.service;

import com.ems.dto.EmployeeDTO;
import com.ems.exception.DuplicateEmailException;
import com.ems.exception.EmployeeNotFoundException;
import com.ems.model.Employee;
import com.ems.model.Employee.EmployeeStatus;
import com.ems.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    // ──────────────────────────── CREATE ────────────────────────────

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        log.info("Creating employee with email: {}", dto.getEmail());

        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + dto.getEmail());
        }

        Employee employee = mapToEntity(dto);
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created with ID: {}", saved.getId());
        return mapToDTO(saved);
    }

    // ──────────────────────────── READ ────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        return mapToDTO(findEmployeeById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> searchEmployees(String query, Pageable pageable) {
        return employeeRepository.searchEmployees(query, pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getEmployeesByDepartment(String department, Pageable pageable) {
        return employeeRepository.findByDepartmentAndStatus(department, EmployeeStatus.ACTIVE, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status, Pageable pageable) {
        return employeeRepository.findByStatus(status, pageable).map(this::mapToDTO);
    }

    // ──────────────────────────── UPDATE ────────────────────────────

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        log.info("Updating employee ID: {}", id);
        Employee existing = findEmployeeById(id);

        if (employeeRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateEmailException("Email already in use: " + dto.getEmail());
        }

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setDepartment(dto.getDepartment());
        existing.setJobTitle(dto.getJobTitle());
        existing.setSalary(dto.getSalary());
        existing.setHireDate(dto.getHireDate());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        return mapToDTO(employeeRepository.save(existing));
    }

    @Override
    public EmployeeDTO updateEmployeeStatus(Long id, EmployeeStatus status) {
        Employee employee = findEmployeeById(id);
        employee.setStatus(status);
        return mapToDTO(employeeRepository.save(employee));
    }

    // ──────────────────────────── DELETE ────────────────────────────

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee ID: {}", id);
        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
    }

    // ──────────────────────────── STATS ────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveEmployees() {
        return employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getEmployeeCountByDepartment() {
        Map<String, Long> result = new LinkedHashMap<>();
        employeeRepository.countByDepartment()
                .forEach(row -> result.put((String) row[0], (Long) row[1]));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    // ──────────────────────────── HELPERS ────────────────────────────

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
    }

    private EmployeeDTO mapToDTO(Employee e) {
        return EmployeeDTO.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .department(e.getDepartment())
                .jobTitle(e.getJobTitle())
                .salary(e.getSalary())
                .hireDate(e.getHireDate())
                .status(e.getStatus())
                .build();
    }

    private Employee mapToEntity(EmployeeDTO dto) {
        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .department(dto.getDepartment())
                .jobTitle(dto.getJobTitle())
                .salary(dto.getSalary())
                .hireDate(dto.getHireDate())
                .status(dto.getStatus() != null ? dto.getStatus() : EmployeeStatus.ACTIVE)
                .build();
    }
}
