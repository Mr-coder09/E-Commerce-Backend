package com.example.Main.Repository;

import com.example.Main.DTO.AdminUserRow;
import com.example.Main.Entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminUserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT new com.example.Main.DTO.AdminUserRow(
            u.id,
            u.name,
            u.email,
            u.role,
            u.isDeleted,
            u.createdAt
        )
        FROM User u
    """)
    Page<AdminUserRow> findAllUsers(Pageable pageable);
}