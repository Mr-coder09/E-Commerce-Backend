package com.example.Main.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.CouponResponse;
import com.example.Main.DTO.CouponValidateRequest;
import com.example.Main.DTO.CouponValidateResponse;
import com.example.Main.DTO.CreateCouponRequest;

import com.example.Main.Enum.CouponStatus;
import com.example.Main.Service.CouponService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class CouponController {

	@Autowired
	CouponService couponService;


	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/coupons")
	public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest createCouponRequest) {
		// TODO: process POST request
		
		CouponResponse response = couponService.createdCoupon(createCouponRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admin/coupons/{id}/status")
	public ResponseEntity<Void> updateCouponStatus( @PathVariable("id") Long couponId,@RequestParam CouponStatus status ) {
		// TODO: process POST request

		couponService.updateCouponStatus(couponId, status);
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/getAllcoupons")
	public ResponseEntity<java.util.List<CouponResponse>> getAllCoupons() {
		
		return  ResponseEntity.ok(couponService.getAllCoupons());
		
		
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/coupons/{code}")
	public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable String code) {
		
		return ResponseEntity.ok(couponService.getCouponByCode(code));
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/coupons/validate")
	public ResponseEntity<CouponValidateResponse> validateCoupon(@Valid @RequestBody CouponValidateRequest request) {
		//TODO: process POST request
		
		return ResponseEntity.ok(couponService.validateCoupon(request));
	}
	
	
	
	
}
