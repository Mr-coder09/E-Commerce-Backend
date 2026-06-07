package com.example.Main.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminAuditRow {

	
	 private Long auditId;
	    private Long userId;
	    private String userEmail;
	    private String action;
	    private String entityType;
	    private Long entityId;
	    private String status;
	    private String message;
	    private LocalDateTime createdAt;
}
