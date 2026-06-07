package com.example.Main.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Main.DTO.CouponResponse;

import com.example.Main.DTO.CreateCouponRequest;
import com.example.Main.Entity.Coupon;

@Mapper(
		componentModel = "spring")
public interface CouponMapper {

	
	@Mapping(target = "id" , ignore = true)
	@Mapping(target = "status", ignore = true)
    @Mapping(target = "totalUsed", ignore = true)
	Coupon toEntity(CreateCouponRequest request);
	
	
	CouponResponse toResponse(Coupon coupon );
	
}
