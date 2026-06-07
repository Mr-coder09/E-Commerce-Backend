package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;




import com.example.Main.Enum.OrderStatus;
import com.example.Main.Enum.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminOrderRow {
	
	
	
	private long orderId;
	
	private long userId;
	
	
	private String email;

	private OrderStatus orderStatus;
	
	private BigDecimal totalAmount;
	
	private BigDecimal discountedAmount;
	
	private String couponCode;
	

	private PaymentStatus paymentStatus;
	
	
	private LocalDateTime createdAt;
	
	private boolean deleted;


	
}
