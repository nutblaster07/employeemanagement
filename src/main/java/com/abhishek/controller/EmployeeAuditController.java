package com.abhishek.controller;

import com.abhishek.entity.EmployeeAuditLog;
import com.abhishek.service.EmployeeAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class EmployeeAuditController {

    private final EmployeeAuditService employeeAuditService;

    public EmployeeAuditController(
            EmployeeAuditService employeeAuditService) {
        this.employeeAuditService = employeeAuditService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<EmployeeAuditLog>> getAuditLogs(
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(
                employeeAuditService.getLogsByEmployeeId(employeeId)
        );
    }
}