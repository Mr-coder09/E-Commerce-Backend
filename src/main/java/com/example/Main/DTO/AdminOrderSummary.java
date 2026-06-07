package com.example.Main.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminOrderSummary {

	
	private Long totalOrders;
	private Long createdOrders;
	private Long paidOrders;
	private Long cancelledOrders;
	private Long refundedOrders;
	private BigDecimal totalRevenue;
	private BigDecimal todayRevenue;
	
	
		
		
		
		
		
		
}
