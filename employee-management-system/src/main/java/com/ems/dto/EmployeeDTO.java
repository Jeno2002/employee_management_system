package com.ems.dto;

import com.ems.model.Employee.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double salary;

    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;

    private EmployeeStatus status;
}
