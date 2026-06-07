package com.example.Main.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CouponValidateRequest {

	@NotBlank
	private String couponCode;
//	@NotBlank
	@NotNull
    private Long userId;
//	@NotBlank
	@NotNull
    private BigDecimal orderAmount;
	
}
