package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data

public class CartItemsResponse {
	
	private Long id;
	private Long productId;
	private String productName;
	private Integer quantity;
	private BigDecimal priceAtThatTime;
	private LocalDateTime updatedAt;
}
