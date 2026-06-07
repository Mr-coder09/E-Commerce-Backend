package com.example.Main.DTO;

import java.util.List;

import lombok.Data;

@Data
public class AdminCouponUsage {

	
	private Long couponId;
    private String couponCode;
    private Long totalUsed;
    private List<AdminCouponUserUsage> usageByUsers;
}
