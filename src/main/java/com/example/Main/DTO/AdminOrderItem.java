package com.example.Main.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AdminOrderItem {
	
	
	private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtThatTime;
}
