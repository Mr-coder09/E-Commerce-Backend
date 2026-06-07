package com.example.Main.DTO;

import java.time.LocalDateTime;

import com.example.Main.Enum.DiscountType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCouponRequest {

	
	@NotBlank
	private String couponCode;
    @NotNull
	private DiscountType discountType  ;      // PERCENTAGE | FLAT
	@NotNull
	@Min(1)
	private long discountValue          ;     // % or flat amount
	@NotNull
	@Min(0)
	private long maxDiscountedAmount     ;    // cap for percentage
	@NotNull
	@Min(1)
	private long minOrderAmount ;
	@NotNull
	@Min(1)
	private  long maxUsage     ;      // total global usage

@NotNull
@Min(1)
	private int maxUsagePerUser ;
@NotNull
private LocalDateTime validFrom;

@NotNull
private LocalDateTime validTill;
	
	

	
	
	
	
	
}



