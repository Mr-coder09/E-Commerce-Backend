package com.example.Main.Service;

import com.example.Main.DTO.CartResponse;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.Main.DTO.CartItemsRequest;


public interface CartService {

//	CartResponse createCart(Long userId);

	// 1️⃣ Get current user cart
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	CartResponse getMyCart(Long userId);

	// 2️⃣ Add an item to the cart
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	CartResponse addItem(Long userId, CartItemsRequest request);

	// 3️⃣ Update quantity for an item
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	CartResponse updateItem(Long userId, Long cartItemId, int quantity);

	// 4️⃣ Remove an item from cart
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	CartResponse removeItem(Long userId, Long cartItemId);

	// 5️⃣ Clear entire cart
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	void clearCart(Long userId);
}
