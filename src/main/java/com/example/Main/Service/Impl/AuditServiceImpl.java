package com.example.Main.Service.Impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Main.Entity.AuditLog;
import com.example.Main.Repository.AuditLogRepository;
import com.example.Main.Service.AuditService;

@Service
public class AuditServiceImpl implements AuditService {

		@Autowired
	    private AuditLogRepository auditLogRepository;
	
	
	
	
	@Override
	public void log(Long userId, String email, String role, String action, String entityType, Long entityId,
			String status, String message) {
		
		
		 AuditLog log = new AuditLog();
	        log.setUserId(userId);
	        log.setUserEmail(email);
	        log.setRole(role);
	        log.setAction(action);
	        log.setEntityType(entityType);
	        log.setEntityId(entityId);
	        log.setStatus(status);
	        log.setMessage(message);
	        log.setTimestamp(LocalDateTime.now());

	        auditLogRepository.save(log);
		
	}

}
