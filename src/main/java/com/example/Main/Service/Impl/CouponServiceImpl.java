package com.example.Main.Service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.CouponResponse;
import com.example.Main.DTO.CouponValidateRequest;
import com.example.Main.DTO.CouponValidateResponse;
import com.example.Main.DTO.CreateCouponRequest;
import com.example.Main.Entity.Coupon;
import com.example.Main.Entity.CouponUsage;
import com.example.Main.Enum.CouponStatus;
import com.example.Main.Enum.DiscountType;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Mapper.CouponMapper;
import com.example.Main.Repository.CouponRepository;
import com.example.Main.Repository.CouponUagesRepository;
import com.example.Main.Service.CouponService;



@Service
public class CouponServiceImpl implements CouponService {
	
	@Autowired
	CouponRepository couponRepository;
	
	@Autowired
	CouponUagesRepository couponUagesRepository;
	
	@Autowired
	CouponMapper couponMapper;
	
	
	
	
	
	
	
	
	@Transactional
	@Override
	public CouponResponse createdCoupon(CreateCouponRequest request) {
		
		  // Unique code check
        if (couponRepository.existsByCouponCode(request.getCouponCode())) {
            throw new BadRequestException("Coupon code already exists");
        }
		
		
		
		Coupon coupon = couponMapper.toEntity(request);
		coupon.setStatus(CouponStatus.ACTIVE);
		coupon.setTotalUsed(0);
		
		Coupon savedCoupon = couponRepository.save(coupon);
		
		CouponResponse response = couponMapper.toResponse(savedCoupon );
		
		
		
		return response;
	}

	@Transactional
	@Override
	public void updateCouponStatus(Long couponId, CouponStatus status) {
		
		Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow( () -> new IdNotFoundException(" Invalid Coupon  ")
						);
		
		coupon.setStatus(status);
		
		couponRepository.save(coupon);
		
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<CouponResponse> getAllCoupons() {
		
		List<Coupon> coupons = couponRepository.findAll();
		
		List<CouponResponse> responsesList = new ArrayList<CouponResponse>();
		
		for (Coupon coupon : coupons) {
			
			CouponResponse response = couponMapper.toResponse(coupon);
			
			responsesList.add(response);
			
		}
// 		
//		
//		
//		
		
//		return responsesList;
		
		return couponRepository.findAll()
				.stream()
				.map(couponMapper::toResponse)
				.collect(Collectors.toList());
	
		
		
	}
	
	
	
	
	
	
	

	@Transactional(readOnly = true)
	@Override
	public CouponResponse getCouponByCode(String code) {
		
		
		Coupon coupon = couponRepository.findByCouponCode(code)
				.orElseThrow( () -> new BadRequestException("Invalid Coupon Code ")
						);
		
		CouponResponse response = couponMapper.toResponse(coupon);
		
		
		return response;
	}

	
	
	
	
	
	@Transactional(readOnly = true)
	@Override
	public CouponValidateResponse validateCoupon(CouponValidateRequest request) {
		
		
		Coupon coupon = couponRepository.findByCouponCode(request.getCouponCode())
				.orElseThrow( () -> new BadRequestException("Invalid Coupon Code ")
						);
		
		
		if (coupon.getStatus() != (CouponStatus.ACTIVE)) {
			
			return inValid(" Coupon Code is Inactive ");
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		if ( now.isBefore(coupon.getValidFrom()) || now.isAfter(coupon.getValidTill()) ) {
			
			return inValid(" Coupon is expired ");
		}
		
		if (request.getOrderAmount().compareTo(coupon.getMinOrderAmount()) < 0  ) {
			
			return inValid("Order amount is less than minimum required");
		}
		
		
		if ( coupon.getMaxUsage() <= coupon.getTotalUsed() ) {
			
			return inValid(" Coupon Exceed  ");
			
		}
		
		
		
		CouponUsage couponUsage = couponUagesRepository
				.findByUserIdAndCouponId(request.getUserId(),coupon.getId())
				.orElse(null);
		
		
		
		 if (couponUsage != null && couponUsage.getUsageCount() >= coupon.getMaxUsagePerUser()) {
		        return inValid("Coupon usage limit exceeded for this user");
		    }
		
		BigDecimal discount = BigDecimal.ZERO;
		
		if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
			
			BigDecimal  orderAmount = request.getOrderAmount();
			BigDecimal discountValue = coupon.getDiscountValue();
			
			 discount = orderAmount
					.multiply(discountValue)
					.divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP );
		} 
		
		
		else {
			 discount = coupon.getDiscountValue();
			
			
		}
		
		discount = discount.min(coupon.getMaxDiscountedAmount());
		
		BigDecimal finalAmount = request.getOrderAmount().subtract(discount);
		
		

		
		CouponValidateResponse response = new  CouponValidateResponse();
		
		response.setDiscountAmount(discount);
		response.setFinalPayableAmount(finalAmount);
		response.setMessage(" Applied Successfully ");
		response.setValid(true);
		response.setCouponCode(coupon.getCouponCode());
		response.setCouponId(coupon.getId());
		return response;
	}

	
	
	
	private CouponValidateResponse inValid(String message) {
		
CouponValidateResponse response = new  CouponValidateResponse();
		
		response.setDiscountAmount(BigDecimal.ZERO);
		response.setFinalPayableAmount(null);
		response.setMessage(message);
		response.setValid(false);
		response.setCouponCode(null);
		response.setCouponId(null);

		return response;
		
		
		
	}
	
	
	
	
	
	
}
