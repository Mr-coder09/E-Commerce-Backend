package com.example.Main.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

}
