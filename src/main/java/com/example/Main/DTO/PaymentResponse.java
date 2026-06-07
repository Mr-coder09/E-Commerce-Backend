package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;




import com.example.Main.Enum.PaymentMethod;
import com.example.Main.Enum.PaymentStatus;


import lombok.Data;

@Data
public class PaymentResponse {

	private long id;
	
	private long orderId;
	

	private BigDecimal amount;
	
    
	private String Currency ;
	
	private PaymentStatus status;
	
	
	private PaymentMethod method;
	
	
	private String gatewayPaymentId;
	
	private String gatewayOrderId;
	
	private long transactionId;

	

	private LocalDateTime createdAt;


	private LocalDateTime updatedAt;
	
}
