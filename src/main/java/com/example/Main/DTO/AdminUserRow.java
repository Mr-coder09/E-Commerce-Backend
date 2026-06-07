package com.example.Main.DTO;

import java.time.LocalDateTime;

import com.example.Main.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserRow {

    private Long userId;
    private String name;
    private String email;
    private Role role;
    private Boolean deleted;
    private LocalDateTime createdAt;

}