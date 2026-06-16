package com.bpmplatform.api.controllers;

import com.bpmplatform.api.dto.ApiResponse;
import com.bpmplatform.api.dto.audit.AuditLogResponse;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import com.bpmplatform.infrastructure.persistence.AuditLogRepository;
import com.bpmplatform.security.infrastructure.auth.BpmUserDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    public AuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogs(
            @AuthenticationPrincipal BpmUserDetails user,
            @RequestParam(defaultValue = "50") int limit) {
        var schema = "tenant_" + user.getTenantId().toString().replace("-", "");
        TenantContext.setTenantId(schema);
        try {
            var page = PageRequest.of(0, Math.min(limit, 200), Sort.by(Sort.Direction.DESC, "createdAt"));
            var logs = auditLogRepository.findAll(page).stream()
                    .map(log -> new AuditLogResponse(log.getId(), log.getUserId(), log.getAction(),
                            log.getEntityType(), log.getEntityId(), log.getDetailsJson(),
                            log.getIpAddress(), log.getCreatedAt()))
                    .toList();
            return ResponseEntity.ok(ApiResponse.ok("Audit logs retrieved", logs));
        } finally {
            TenantContext.clear();
        }
    }
}
