package com.example.Main.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.CartItemsRequest;
import com.example.Main.DTO.CartResponse;

import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Tag(name = "Cart APIs ",
description = " APIs for manging user shopping cart operations")


@RestController
@RequestMapping("/api/v1")

public class CartController {
	
	@Autowired
	CartService cartService;
	
	
	private long getUserId(HttpServletRequest req) {
	    Object userId = req.getAttribute("userId");
	    if (userId == null) {
	        throw new UnauthorizedAccessException("Access Denied");
	    }
	    return (Long) userId;
	}
	
	@Operation(
	        summary = "Get my cart",
	        description = "Fetches the authenticated user's active cart. Creates a cart if none exists."
	    )
	
	@GetMapping("/cart")
	public ResponseEntity<CartResponse > getMyCart(HttpServletRequest req) {
		
		
			long userId = getUserId(req);
			
			
			CartResponse cartResponse =  cartService.getMyCart(userId);
			
			return ResponseEntity.ok(cartResponse);
		
		

	}
	
	
	
	@Operation(
	        summary = "Add item to cart",
	        description = "Adds a product to the user's cart or increases quantity if already present"
	    )

	@PostMapping("/cart/items")
	public ResponseEntity<CartResponse> addItems(HttpServletRequest req ,@Valid @RequestBody CartItemsRequest cartItemsRequest ) {
		
		
			
			long userId = getUserId(req);
			CartResponse cartResponse = cartService.addItem(userId, cartItemsRequest);
			
			
			
			return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
		
		
		
	}
	
	
	
	
	@PutMapping("/cart/items/{cartItemId}")
	public ResponseEntity<CartResponse> updateCartItems(HttpServletRequest req  ,
			@PathVariable Long cartItemId,
			@RequestParam  int qty ) {
		
		
		
			long userId = getUserId(req);
			
			
				CartResponse cartResponse =  cartService.updateItem(userId, cartItemId, qty);
				
				return ResponseEntity.ok(cartResponse);
			
			
		

	}
	
	
	
	
	
	@DeleteMapping("/cart/items/{cartItemId}")
	public ResponseEntity<CartResponse>  removeCartItem(HttpServletRequest req ,
			@PathVariable Long cartItemId){
		
		
			long userId = getUserId(req);
			
			
				CartResponse cartResponse =  cartService.removeItem(userId, cartItemId);
				
				return ResponseEntity.ok(cartResponse);
			
		
		
	}
	
	@DeleteMapping("/cart/items")
	public ResponseEntity<CartResponse>  clearCart(HttpServletRequest req ){
		
		
			long userId = getUserId(req);
			
			
				cartService.clearCart(userId);
				
				return ResponseEntity.noContent().build(); 
			
		
		
	}
	
	
	
	
	
	
}
