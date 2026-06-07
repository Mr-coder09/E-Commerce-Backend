package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderItemResponse {

	
	private long id;
	private long productId;
	private String productName;
	private Integer quantity;
	private BigDecimal priceAtThatTime;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
