package com.example.Main.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who performed action
    private Long userId;

    private String userEmail;
    private String role;

    // What happened
    private String action;       // CREATE_ORDER, UPDATE_ORDER_STATUS
    private String entityType;   // ORDER, CART, USER
    private Long entityId;

    private String status;       // SUCCESS / FAILED
    private String message;

    private LocalDateTime timestamp;
}