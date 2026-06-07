package com.example.Main.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Request payload for placing an order")
@Data
public class OrderCheckoutRequest {
	
	@NotBlank @Schema(description = "Shipping address", example = "Hyderabad, India")
	 @Size(min=10, max=300)
	private String shippingAddress;
	
	private String couponCode;
	
}
