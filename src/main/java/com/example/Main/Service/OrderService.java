package com.example.Main.Service;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.Main.DTO.CouponValidateResponse;
import com.example.Main.DTO.OrderCheckoutRequest;
import com.example.Main.DTO.OrderResponse;

public interface OrderService {
	
	@PreAuthorize("hasRole('CUSTOMER')")
//	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	 OrderResponse checkOutOrder(Long userId , OrderCheckoutRequest orderCheckoutRequest );
	 
	
	
	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	 Page<OrderResponse> getMyOrder(Long userId , 
			 int page ,
			 int size,
			 String sortBy,
			 String direction);
	 
	
	
	
	
	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	 OrderResponse getOrderById(Long userId,Long orderId);
	 
	@PreAuthorize("hasRole('ADMIN')")
	 OrderResponse updateOrder(Long orderId,String status);
	 
	@PreAuthorize("hasRole('CUSTOMER')")
	CouponValidateResponse applyCouponToOrder(Long orderId, Long userId, String couponCode);

	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	void deleteOrder(long orderId, long userId);
	
}
