package com.example.Main.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminCouponUserUsage {

    private Long userId;
    private String userEmail;
    private Long usageCount;
    private LocalDateTime lastUsedAt;

 

 
}