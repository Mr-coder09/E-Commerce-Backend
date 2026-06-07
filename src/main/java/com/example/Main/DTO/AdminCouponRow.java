package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.Main.Enum.CouponStatus;
import com.example.Main.Enum.DiscountType;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminCouponRow {

	
	
	private long couponId;
	
	private String couponCode;
	
	private DiscountType discountType;
	
	private BigDecimal discountValue;
	
	private long maxUsage;
	
	private long totalUsed;
	
	private int maxUsagePerUser;
	
	private CouponStatus couponStatus;
	
	
	private LocalDateTime validFrom;
	

	private LocalDateTime validTill;

}
