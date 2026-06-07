package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;



import com.example.Main.Enum.CouponStatus;
import com.example.Main.Enum.DiscountType;


import lombok.Data;

@Data
public class CouponResponse {

	
	
	private long id;
	
	private String CouponCode;
	

	private BigDecimal discountValue;
	
	
	private BigDecimal maxDiscountedAmount;
	
	
	private BigDecimal minOrderAmount;
	

	private LocalDateTime validFrom;
	
	
	private LocalDateTime validTill;
	
	
	private long maxUsage;
	
	private long totalUsed;
	
	private int maxUsagePerUser;
	

	private DiscountType discountType;

	private CouponStatus status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	
	
	
}
