
package com.abhishek.service;
import com.abhishek.service.EmployeeAuditService;
import org.springframework.transaction.annotation.Transactional;

import com.abhishek.entity.EmployeeAuditLog;
import com.abhishek.repository.EmployeeAuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeAuditService {

    private final EmployeeAuditLogRepository auditLogRepository;

    public EmployeeAuditService(
            EmployeeAuditLogRepository auditLogRepository
    ) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logStatusChange(
            Long employeeId,
            String employeeName,
            String previousStatus,
            String newStatus
    ) {
        EmployeeAuditLog log = new EmployeeAuditLog(
                employeeId,
                employeeName,
                previousStatus,
                newStatus,
                "Employee status changed to INACTIVE"
        );

        auditLogRepository.save(log);
    }

    public List<EmployeeAuditLog> getLogsByEmployeeId(Long employeeId) {
        return auditLogRepository
                .findByEmployeeIdOrderByChangedAtDesc(employeeId);
    }
}
