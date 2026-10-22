
package com.abhishek.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_audit_logs")
public class EmployeeAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String employeeName;

    private String previousStatus;

    private String newStatus;

    private String action;

    private LocalDateTime changedAt;

    public EmployeeAuditLog() {
    }

    public EmployeeAuditLog(
            Long employeeId,
            String employeeName,
            String previousStatus,
            String newStatus,
            String action
    ) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.action = action;
        this.changedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}
