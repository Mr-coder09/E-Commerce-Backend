package com.example.Main.Service;

public interface AuditService {

	
	 void log(
		        Long userId,
		        String email,
		        String role,
		        String action,
		        String entityType,
		        Long entityId,
		        String status,
		        String message
		    );
}
