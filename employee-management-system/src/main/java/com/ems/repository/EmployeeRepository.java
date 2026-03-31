package com.ems.repository;

import com.ems.model.Employee;
import com.ems.model.Employee.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find by email
    Optional<Employee> findByEmail(String email);

    // Check if email exists (excluding current employee by id)
    boolean existsByEmailAndIdNot(String email, Long id);

    // Search employees by name or email
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Employee> searchEmployees(@Param("query") String query, Pageable pageable);

    // Find by department
    List<Employee> findByDepartment(String department);

    // Find by status
    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);

    // Find by department and status
    Page<Employee> findByDepartmentAndStatus(String department, EmployeeStatus status, Pageable pageable);

    // Count by department
    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countByDepartment();

    // Count by status
    long countByStatus(EmployeeStatus status);

    // Get all distinct departments
    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();
}
