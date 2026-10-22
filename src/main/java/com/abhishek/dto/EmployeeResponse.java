
package com.abhishek.dto;

public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String department;
    private double salary;
    private String status;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String name, String email,
                            String department, double salary,
                            String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getStatus() {
        return status;
    }
}
