package com.example.Main.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CouponValidateResponse {

    private Long couponId;
    private String couponCode;

    private BigDecimal discountAmount;
    private BigDecimal finalPayableAmount;

    private boolean valid;
    private String message;
}