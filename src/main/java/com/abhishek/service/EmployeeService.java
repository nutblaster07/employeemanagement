
package com.abhishek.service;

import com.abhishek.entity.Employee;
import com.abhishek.exception.ResourceNotFoundException;
import com.abhishek.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeAuditService employeeAuditService;
    private final EmailNotificationService emailNotificationService;

    // Constructor injection
    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeAuditService employeeAuditService,
            EmailNotificationService emailNotificationService
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeAuditService = employeeAuditService;
        this.emailNotificationService = emailNotificationService;
    }

    // Create employee
    public Employee createEmployee(Employee employee) {

        if (employeeRepository.existsByEmailIgnoreCase(
                employee.getEmail())) {
            throw new IllegalArgumentException(
                    "An employee with this email already exists"
            );
        }

        return employeeRepository.save(employee);
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with ID: " + id
                ));
    }

    // Update employee and trigger notifications on deactivation
    @Transactional
    public Employee updateEmployee(
            Long id,
            Employee updatedEmployee
    ) {
        Employee existingEmployee = getEmployeeById(id);

        // Prevent duplicate email addresses
        if (!existingEmployee.getEmail().equalsIgnoreCase(
                updatedEmployee.getEmail())
                && employeeRepository.existsByEmailIgnoreCase(
                updatedEmployee.getEmail())) {

            throw new IllegalArgumentException(
                    "An employee with this email already exists"
            );
        }

        String previousStatus = existingEmployee.getStatus();
        String newStatus = updatedEmployee.getStatus();

        // Trigger notifications only for ACTIVE -> INACTIVE
        boolean becameInactive =
                "ACTIVE".equalsIgnoreCase(previousStatus)
                        && "INACTIVE".equalsIgnoreCase(newStatus);

        // Update employee fields
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        existingEmployee.setStatus(newStatus);

        Employee savedEmployee =
                employeeRepository.save(existingEmployee);

        // Audit and email notification
        if (becameInactive) {
            employeeAuditService.logStatusChange(
                    savedEmployee.getId(),
                    savedEmployee.getName(),
                    previousStatus,
                    newStatus
            );

            // Returns false if SendGrid is not configured or sending fails.
            // Persistent retries will be added in a later stage.
            emailNotificationService.sendInactiveNotification(
                    savedEmployee
            );
        }

        return savedEmployee;
    }

    // Delete employee
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    // Search employees by name
    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }

    // Filter employees by department
    public List<Employee> getEmployeesByDepartment(
            String department
    ) {
        return employeeRepository
                .findByDepartmentIgnoreCase(department);
    }
}
