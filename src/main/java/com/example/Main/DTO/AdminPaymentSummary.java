package com.example.Main.DTO;

import java.math.BigDecimal;


import lombok.Data;

@Data
//@AllArgsConstructor
public class AdminPaymentSummary {

	private Long totalPayments;
	private Long successfulPayments;
	private Long failedPayments;
	private Long refundedPayments;
	private BigDecimal totalCollected;
	private BigDecimal totalRefunded;
	private BigDecimal netRevenue;
	
	
		
	public AdminPaymentSummary(
			Long totalPayments,
			Long successfulPayments,
			 Long failedPayments,
			 Long refundedPayments,
			 BigDecimal totalCollected,
			 BigDecimal totalRefunded
			) {
		this.totalPayments = totalPayments;
        this.successfulPayments = successfulPayments;
        this.failedPayments = failedPayments;
        this.refundedPayments = refundedPayments;
        this.totalCollected = totalCollected;
        this.totalRefunded = totalRefunded;
        
        this.netRevenue = totalCollected.subtract(totalRefunded);
		
		
		
	}
		
		
		
		
		
}
