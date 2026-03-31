package com.ems.controller;

import com.ems.dto.EmployeeDTO;
import com.ems.model.Employee.EmployeeStatus;
import com.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    // ──────────────────────────── DASHBOARD ────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEmployees", employeeService.getTotalEmployees());
        model.addAttribute("activeEmployees", employeeService.getActiveEmployees());
        model.addAttribute("departments", employeeService.getAllDepartments());
        model.addAttribute("deptCounts", employeeService.getEmployeeCountByDepartment());
        return "employee/dashboard";
    }

    // ──────────────────────────── LIST ────────────────────────────

    @GetMapping
    public String listEmployees(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeDTO> employeePage;
        if (!search.isBlank()) {
            employeePage = employeeService.searchEmployees(search, pageable);
        } else if (!department.isBlank()) {
            employeePage = employeeService.getEmployeesByDepartment(department, pageable);
        } else {
            employeePage = employeeService.getAllEmployees(pageable);
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("department", department);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("departments", employeeService.getAllDepartments());
        return "employee/list";
    }

    // ──────────────────────────── VIEW ────────────────────────────

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee/view";
    }

    // ──────────────────────────── CREATE ────────────────────────────

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("isEdit", false);
        return "employee/form";
    }

    @PostMapping("/new")
    public String createEmployee(
            @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "employee/form";
        }

        try {
            EmployeeDTO created = employeeService.createEmployee(employeeDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employee '" + created.getFirstName() + " " + created.getLastName() + "' created successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", false);
            return "employee/form";
        }
    }

    // ──────────────────────────── EDIT ────────────────────────────

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("isEdit", true);
        return "employee/form";
    }

    @PostMapping("/{id}/edit")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "employee/form";
        }

        try {
            EmployeeDTO updated = employeeService.updateEmployee(id, employeeDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employee '" + updated.getFirstName() + " " + updated.getLastName() + "' updated successfully!");
            return "redirect:/employees";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", EmployeeStatus.values());
            model.addAttribute("isEdit", true);
            return "employee/form";
        }
    }

    // ──────────────────────────── DELETE ────────────────────────────

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }

    // ──────────────────────────── ROOT REDIRECT ────────────────────────────

    @GetMapping("/")
    public String root() {
        return "redirect:/employees/dashboard";
    }
}
