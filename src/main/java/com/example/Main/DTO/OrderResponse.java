package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.Main.Enum.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {

	private long id;
	private BigDecimal totalAmount;
	private OrderStatus status;
	private String shippingAddress;
	
	private List<OrderItemResponse> orderItems;

	private BigDecimal discountedAmount;

	private BigDecimal finalAmount;

	private String couponCode;

	private long couponId;

	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
}
