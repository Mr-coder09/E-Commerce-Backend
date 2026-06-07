package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


import com.example.Main.Enum.OrderStatus;
import com.example.Main.Enum.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AdminOrderDetail {


	private long orderId;
	
	private long userId;

	private OrderStatus orderStatus;
	
	private BigDecimal totalAmount;
	
	private BigDecimal discountedAmount;
	
	private BigDecimal finalAmount;
	
	private String couponCode;
	
	private List<AdminOrderItem> orderItems;

	private PaymentStatus paymentStatus;
	
	
	private LocalDateTime createdAt;
	
	
	private LocalDateTime updatedAt;
	
	private boolean deleted;
	


	
	
	
	
}
