package com.example.Main.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.OrderCheckoutRequest;
import com.example.Main.DTO.OrderResponse;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	private long getUserId(HttpServletRequest req) {
		
		
		Long userId = (Long) req.getAttribute("userId");
		if (userId == null) {
			 throw new UnauthorizedAccessException("Access Denied");
		}
		
		
		return userId;
	}
	
	
	@PostMapping("/orders/checkout")
	public ResponseEntity<OrderResponse> checkOut(HttpServletRequest request , 
			@Valid @RequestBody OrderCheckoutRequest orderCheckoutRequest){
		
		Long userId = getUserId(request);
		
		OrderResponse orderResponse = orderService.checkOutOrder(userId, orderCheckoutRequest);
		
		return ResponseEntity.ok(orderResponse);
		
		
	}
	@GetMapping("/orders")
	public ResponseEntity<Page<OrderResponse>> getMyorders(HttpServletRequest request,
			@RequestParam(defaultValue = "0 ") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "DESC") String direction
			){
		
		
		Long userId = getUserId(request);
		
		Page<OrderResponse> orderResponse = orderService.getMyOrder(userId, page, size, sortBy, direction);
//		List<OrderResponse> orderResponse = orderService.getMyOrder(userId);
		return ResponseEntity.ok(orderResponse);
		
	}
	
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(HttpServletRequest request, @PathVariable Long orderId ){
		
		Long userId = getUserId(request);
		
		
		OrderResponse orderResponse = orderService.getOrderById(userId, orderId);
		return ResponseEntity.ok(orderResponse);
		
		
	}
	
	
	
	@PutMapping("admin/orders/{orderId}")
	public ResponseEntity<OrderResponse> updateOrders(
			@PathVariable Long orderId,
			@RequestParam String status){
		
		
		
//		String adminEmail = (String) request.getAttribute("email");
				
				
//		if (adminEmail == null) {
//			 throw new UnauthorizedAccessException("Access Denied");
//		}		
				
		if (status == null || status.trim().isEmpty()) {
		    throw new BadRequestException("Status must not be empty");
		}
		
		OrderResponse orderResponse = orderService.updateOrder( orderId, status);
		return ResponseEntity.ok(orderResponse); 
		
		
		
	}
	
	
	
}
