package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class AdminUserDetail {

	private Long userId;
    private String name;
    private String email;
    private String role;

    private Long totalOrders;
    private BigDecimal totalSpent;
    private Integer couponsUsed;

    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
}
