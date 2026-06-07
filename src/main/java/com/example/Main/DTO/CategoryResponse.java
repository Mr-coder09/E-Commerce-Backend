package com.example.Main.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime updatedAt;
}