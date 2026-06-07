package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;



import com.example.Main.Enum.PaymentMethod;
import com.example.Main.Enum.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminPaymentRow {

	private long paymentId;
	
	private long orderId;
	private long userId;
	private BigDecimal amount;
	private PaymentMethod paymentMethod;
	 
	private PaymentStatus paymentStatus;
	
	private String gatewayPaymentId;
	
	
	private LocalDateTime createdAt;
	
	
	

}
