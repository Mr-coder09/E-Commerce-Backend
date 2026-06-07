package com.example.Main.Mapper;

import org.mapstruct.Mapper;

import com.example.Main.DTO.PaymentResponse;
import com.example.Main.Entity.Payment;

@Mapper( componentModel = "spring" )
public interface PaymentMapper {
	
	
	PaymentResponse toResponse(Payment payment );

}
