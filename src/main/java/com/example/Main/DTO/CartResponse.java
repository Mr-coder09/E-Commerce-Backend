package com.example.Main.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



import com.example.Main.Enum.CartStatus;

import lombok.Data;


@Data
public class CartResponse {

	private Long id;

	
	
	private BigDecimal totalAmount;

	private CartStatus status;

	private List<CartItemsResponse> cartItems;
	
	private LocalDateTime updatedAt;

}
