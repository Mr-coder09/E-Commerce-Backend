package com.example.Main.Service;

import java.util.List;

import com.example.Main.DTO.CouponResponse;
import com.example.Main.DTO.CouponValidateRequest;
import com.example.Main.DTO.CouponValidateResponse;
import com.example.Main.DTO.CreateCouponRequest;
import com.example.Main.Enum.CouponStatus;

public interface CouponService {

	CouponResponse createdCoupon(CreateCouponRequest request );
	
	void updateCouponStatus(Long couponId, CouponStatus status);
	
	List<CouponResponse> getAllCoupons();
	
	CouponResponse getCouponByCode(String code);
	
	CouponValidateResponse validateCoupon(CouponValidateRequest request );
	
	
	
	
}
