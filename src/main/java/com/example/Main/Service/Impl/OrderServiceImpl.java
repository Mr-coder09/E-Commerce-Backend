package com.example.Main.Service.Impl;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.example.Main.DTO.CouponValidateRequest;
import com.example.Main.DTO.CouponValidateResponse;
import com.example.Main.DTO.OrderCheckoutRequest;

import com.example.Main.DTO.OrderResponse;
import com.example.Main.Entity.Cart;
import com.example.Main.Entity.CartItems;
import com.example.Main.Entity.Coupon;
import com.example.Main.Entity.CouponUsage;
import com.example.Main.Entity.Order;
import com.example.Main.Entity.OrderItems;
import com.example.Main.Entity.Product;
import com.example.Main.Entity.User;
import com.example.Main.Enum.CartStatus;
import com.example.Main.Enum.OrderStatus;
import com.example.Main.Enum.Role;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.HelperClass.OrderStatusTransition;
import com.example.Main.HelperClass.RecalculateTotalAmount;
import com.example.Main.Mapper.OrderMapper;
import com.example.Main.Repository.CartRepository;
import com.example.Main.Repository.CouponRepository;
import com.example.Main.Repository.CouponUagesRepository;
import com.example.Main.Repository.OrderItemRepository;
import com.example.Main.Repository.OrderRepository;
import com.example.Main.Repository.ProductRepository;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Security.Hibernateconfig;
import com.example.Main.Service.AuditService;
import com.example.Main.Service.CouponService;
import com.example.Main.Service.OrderService;




@Service
public class OrderServiceImpl implements OrderService {
	
	
	

	@Autowired
	CartRepository cartRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	CouponRepository  couponRepository; 
	
	@Autowired
	CouponUagesRepository couponUagesRepository;
	
	@Autowired
	Hibernateconfig hibernateconfig;
	
	private static final Logger log =
			LoggerFactory.getLogger(OrderServiceImpl.class);

	
	@Transactional
	@Override
	public OrderResponse checkOutOrder(Long userId, OrderCheckoutRequest request) {

		Cart activeCart = cartRepository.findByUser_IdAndStatus(userId, CartStatus.ACTIVE)
				.orElseThrow(() -> new IdNotFoundException("No Active Order "));


		
		
		if (activeCart.getCartItems().isEmpty()) {
			log.warn("Checkout Blocked : userId = {} ,reason = {}",userId ,"Cart is empty" );
			
			
			auditService.log(
				    userId,
				    activeCart.getUser().getEmail(),
				    activeCart.getUser().getRole().name(),
				    "CHECKOUT_ORDER",
				    "ORDER",
				    null,
				    "FAILED",
				    "Checkout attempted with empty cart"
				);
			throw new BadRequestException("Order is empty ");
			
			
		}

		List<CartItems> cartItems = activeCart.getCartItems();

		for (CartItems items : cartItems) {

			Product product = productRepository.findById(items.getProduct().getId())
					.orElseThrow(() -> new IdNotFoundException("Product not found "));

			if (items.getQuantity() > product.getStockQuantity()) {
				log.warn("Checkout blocked: userId={}, reason={}",
				        userId, "INSUFFICIENT_STOCK");
				
				
				auditService.log(
					    userId,
					    activeCart.getUser().getEmail(),
					    activeCart.getUser().getRole().name(),
					    "CHECKOUT_ORDER",
					    "ORDER",
					    null,
					    "FAILED",
					    "Insufficient stock during checkout"
					);
				throw new BadRequestException("Insufficient stock only : " + product.getStockQuantity() + "left");
				
				
			}
		}

		Order order = new Order();

		order.setUser(activeCart.getUser());


		order.setTotalAmount(RecalculateTotalAmount.calculateTotalAmount(cartItems));
		order.setStatus(OrderStatus.CREATED);
		order.setShippingAddress(request.getShippingAddress());


		List<OrderItems> orderItems = new ArrayList<OrderItems>();

		for (CartItems items : cartItems) {
			OrderItems orderItems2 = new OrderItems();

			orderItems2.setOrder(order);
			orderItems2.setProduct(items.getProduct());
			orderItems2.setQuantity(items.getQuantity());
			orderItems2.setPriceAtThatTime(items.getPriceAtThatTime());

			orderItems.add(orderItems2);

		}

		order.setOrderItems(orderItems);

		Order savedOrder = orderRepository.save(order);
			// saving child
		for (OrderItems items : orderItems) {
			items.setOrder(savedOrder);
		}

		orderItemRepository.saveAll(orderItems);

//		for (CartItems items : cartItems) {
//
//			Product product = productRepository.findById(items.getProduct().getId())
//					.orElseThrow(() -> new IdNotFoundException("Product Not found "));
//
//			product.setStockQuantity(product.getStockQuantity() - items.getQuantity());
//
//			productRepository.save(product);
//			
//			
//
//		}
		
//		activeCart.getCartItems().clear();
//		activeCart.setTotalAmount(BigDecimal.ZERO);
		activeCart.setStatus(CartStatus.CHECKED_OUT);
		activeCart.setUpdatedAt(LocalDateTime.now());
		
		cartRepository.save(activeCart);
		
		
		String couponCode = request.getCouponCode() ;
		
		if (couponCode !=null && !couponCode.isBlank()) {
			
			applyCouponToOrder(savedOrder.getId(), userId, couponCode);

		}
		
		
		

//		OrderResponse response = new OrderResponse();
//		response.setId(savedOrder.getId());
//		response.setTotalAmount(savedOrder.getTotalAmount());
//		response.setStatus(savedOrder.getStatus());
//		response.setShippingAddress(savedOrder.getShippingAddress());
//		response.setUpdatedAt(savedOrder.getUpdatedAt());
//		response.setCreatedAt(savedOrder.getCreatedAt());
//
//		List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
//
//		for (OrderItems items : orderItems) {
//
//			OrderItemResponse orderItemResponse = new OrderItemResponse();
//			orderItemResponse.setId(items.getId());
//			orderItemResponse.setProductId(items.getProductId());
//			orderItemResponse.setProductName(items.getProductName());
//			orderItemResponse.setQuantity(items.getQuantity());
//			orderItemResponse.setPriceAtThatTime(items.getPriceAtThatTime());
//			orderItemResponse.setCreatedAt(items.getCreatedAt());
//
//			orderItemResponses.add(orderItemResponse);
//
//		}
//		response.setOrderItems(orderItemResponses);

		log.info(" Order Checkout : orderId={} ,userId={} ,amount={} ",
					activeCart.getId(),userId,activeCart.getTotalAmount()
				);
		
		auditService.log(
			    userId,
			    order.getUser().getEmail(),
			    order.getUser().getRole().name(),
			    "CHECKOUT_ORDER",
			    "ORDER",
			    savedOrder.getId(),
			    "SUCCESS",
			    "Order placed successfully"
			);

		return orderMapper.toResponse(savedOrder);
		

	}
	
	
	
	
	
	
	@Transactional(readOnly = true)
	@Override
	public Page<OrderResponse> getMyOrder(Long userId, int page, int size, String sortBy, String direction) {
		
		hibernateconfig.enableSoftDeleteFilter();
		
		
		Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		PageRequest pageRequest = PageRequest.of(page, size , sort);
		
		return  orderRepository.findByUser_Id(userId, pageRequest)
				.map(orderMapper::toResponse);
		
		
		
		

	}
	
	
	
	
//	@Transactional(readOnly = true)
//	@Override
//	public List<OrderResponse> getMyOrder(Long userId) {
//
//		List<Order> orders = orderRepository.findByUser_Id(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//		
//		
//		
//
//		
//		
//		
//		List<OrderResponse> responses = new ArrayList<OrderResponse>();
//
//		for (Order items : orders) {
//
////			OrderResponse orderResponse = new OrderResponse();
////			orderResponse.setId(items.getId());
////
////
////			orderResponse.setTotalAmount(items.getTotalAmount());
////			orderResponse.setStatus(items.getStatus());
////			orderResponse.setShippingAddress(items.getShippingAddress());
////			orderResponse.setUpdatedAt(items.getUpdatedAt());
////			orderResponse.setCreatedAt(items.getCreatedAt());
////
////			List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
////
////			for (OrderItems orderItems : items.getOrderItems()) {
////
////				OrderItemResponse orderItemResponse = new OrderItemResponse();
////				orderItemResponse.setId(orderItems.getId());
////				orderItemResponse.setProductId(orderItems.getProductId());
////				orderItemResponse.setProductName(orderItems.getProductName());
////				orderItemResponse.setQuantity(orderItems.getQuantity());
////				orderItemResponse.setPriceAtThatTime(orderItems.getPriceAtThatTime());
////				orderItemResponse.setCreatedAt(orderItems.getCreatedAt());
////
////				orderItemResponses.add(orderItemResponse);
////
////			}
////
////			orderResponse.setOrderItems(orderItemResponses);
//
////			responses.add(mapToResponse(items));
//			responses.add(orderMapper.toResponse(items));
//			
//
//		}
//
//		return responses;
//	}

	
	
	
	
	@Override
	public OrderResponse getOrderById(Long userId, Long orderId) {

		Order order = orderRepository.findWithItemsById(orderId).orElseThrow(() -> new IdNotFoundException("Order Not Found"));

		if (!order.getUser().getId().equals(userId)) {
			log.warn("Unauthorized order access: userId={}, orderId={}",
			        userId, orderId);
			throw new UnauthorizedAccessException("  Access Denied ");
		}

//		OrderResponse response = new OrderResponse();
//		response.setId(order.getId());
//
//		response.setTotalAmount(order.getTotalAmount());
//
//		response.setStatus(order.getStatus());
//		response.setShippingAddress(order.getShippingAddress());
//
//		List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
//
//		for (OrderItems items : order.getOrderItems()) {
//
//			OrderItemResponse orderItemResponse = new OrderItemResponse();
//			orderItemResponse.setId(items.getId());
//			orderItemResponse.setProductId(items.getProductId());
//			orderItemResponse.setProductName(items.getProductName());
//			orderItemResponse.setQuantity(items.getQuantity());
//			orderItemResponse.setPriceAtThatTime(items.getPriceAtThatTime());
//			orderItemResponse.setCreatedAt(items.getCreatedAt());
//			orderItemResponses.add(orderItemResponse);
//
//		}
//
//		response.setOrderItems(orderItemResponses);
//
//		response.setCreatedAt(order.getCreatedAt());
//		response.setUpdatedAt(order.getUpdatedAt());
//		return mapToResponse(order);
		return orderMapper.toResponse(order);

	}

	
	@Transactional
	@Override
	public OrderResponse updateOrder( Long orderId, String status) {
		
		
		
		
		
		
		Order existingOrder =  orderRepository.findById(orderId)
				.orElseThrow(()-> new IdNotFoundException("Order Not Found "));
		
//		User userData =  userRepository.findByEmail(email)
//				.orElseThrow(()-> new IdNotFoundException("User Not Found "));
		
//		User userData =  userRepository.findById(userId)
//				.orElseThrow(()-> new IdNotFoundException("User Not Found "));
		
		
//		if (userData.getRole() != Role.ADMIN) {
//			throw new UnauthorizedAccessException("Access Denied ");
//		}
		
	
		
		OrderStatus requestedStatus = OrderStatus.fromString(status);

		 OrderStatusTransition.validateTransition(existingOrder.getStatus(), requestedStatus);

		
		existingOrder.setStatus(requestedStatus);
		existingOrder.setUpdatedAt(LocalDateTime.now());
		orderRepository.save(existingOrder);
		
//		OrderResponse response = new OrderResponse();
//		response.setId(existingOrder.getId());
//		response.setTotalAmount(existingOrder.getTotalAmount());
//		response.setStatus(existingOrder.getStatus());
//		response.setShippingAddress(existingOrder.getShippingAddress());
//		response.setUpdatedAt(existingOrder.getUpdatedAt());
//		response.setCreatedAt(existingOrder.getCreatedAt());
//		
//		List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
//
//		for (OrderItems items : existingOrder.getOrderItems()) {
//
//			OrderItemResponse orderItemResponse = new OrderItemResponse();
//			orderItemResponse.setId(items.getId());
//			orderItemResponse.setProductId(items.getProductId());
//			orderItemResponse.setProductName(items.getProductName());
//			orderItemResponse.setQuantity(items.getQuantity());
//			orderItemResponse.setPriceAtThatTime(items.getPriceAtThatTime());
//			orderItemResponse.setCreatedAt(items.getCreatedAt());
//
//			orderItemResponses.add(orderItemResponse);
//
//		}
//		response.setOrderItems(orderItemResponses);
		
		log.info(
			    "ADMIN updated order status: orderId={}, from={}, to={}",
			    orderId,
			    existingOrder.getStatus(),
			    requestedStatus
			);
		
		
		auditService.log(
				existingOrder.getUser().getId(),
				existingOrder.getUser().getEmail(),
				"ADMIN",
			    "UPDATE_ORDER_STATUS",
			    "ORDER",
			    orderId,
			    "SUCCESS",
			    "Order status updated from " + existingOrder.getStatus() + " to " + requestedStatus
			);

//		return mapToResponse(existingOrder);
		return orderMapper.toResponse(existingOrder);
		
		
		
		
	}










	@Transactional
	@Override
	public CouponValidateResponse applyCouponToOrder(Long orderId, Long userId, String couponCode) {
		
		
		// TODO Auto-generated method stub
		
		
		
		
		Order order = orderRepository
			    .findByUser_IdAndId(userId, orderId)
			    .orElseThrow(() -> new BadRequestException(" Order is not belong to user  ") );
			    
			   
		if (order.getStatus() != OrderStatus.CREATED) {
			
			throw new BadRequestException(" Order Does not exceeds ");
		}
		
		if (order.getCouponCode() != null) {
		    throw new BadRequestException("Coupon already applied on this order");
		}
		
		
		
		CouponValidateRequest couponValidateRequest = new CouponValidateRequest();
		couponValidateRequest.setCouponCode(couponCode);
		couponValidateRequest.setUserId(userId);
		couponValidateRequest.setOrderAmount(order.getTotalAmount());
		
		CouponValidateResponse couponValidateResponse = couponService.validateCoupon(couponValidateRequest);
		
		
		
		
		 if (!couponValidateResponse.isValid()) {
		        throw new BadRequestException(couponValidateResponse.getMessage());
		    }
		
		 
			order.setCouponCode(couponCode);
			order.setDiscountedAmount(couponValidateResponse.getDiscountAmount());
			order.setFinalAmount(couponValidateResponse.getFinalPayableAmount());
			orderRepository.save(order);

		 
		
		Coupon coupon = couponRepository
		        .findByCouponCode(couponCode)
		        .orElseThrow(() -> new BadRequestException("Invalid Coupon Code"));
		
		
		CouponUsage couponUsage = couponUagesRepository
		        .findByUserIdAndCouponId(userId, coupon.getId())
		        .orElse(null);
		
		
		if (couponUsage == null) {
		    couponUsage = new CouponUsage();
		    couponUsage.setUser(order.getUser());
		   
		    couponUsage.setCoupon(coupon);
		    couponUsage.setUsageCount(1);
		}
//		else {
//		    couponUsage.setUsageCount(couponUsage.getUsageCount() + 1);
//		}
//
//		
//		couponUagesRepository.save(couponUsage);
//		
//		
//		coupon.setTotalUsed(coupon.getTotalUsed() + 1);
//		couponRepository.save(coupon);
		
		
		 auditService.log(
		            userId,
		            order.getUser().getEmail(),
		            order.getUser().getRole().name(),
		            "APPLY_COUPON",
		            "ORDER",
		            order.getId(),
		            "SUCCESS",
		            "Coupon applied successfully"
		    );
		
		
//		return couponService.validateCoupon(couponValidateRequest);
		return couponValidateResponse;
		
		
		
		
		
		
		
		
		
	}










	@Transactional
	@Override
	public void deleteOrder(long orderId, long userId) {
		
		
		hibernateconfig.enableSoftDeleteFilter();
		
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IdNotFoundException("Order Not Found ") );
		
//		
		
		User user = userRepository.findById(userId)
		        .orElseThrow(() -> new IdNotFoundException("User not found"));
		
		
//		if (order.isDeleted()) {
//			
//			throw new BadRequestException("Order already deleted");
//		}
//		

		
		
		
		 if (order.getStatus() != OrderStatus.CREATED
		            && order.getStatus() != OrderStatus.CANCELLED) {
		        throw new BadRequestException(
		                "Order cannot be deleted in status: " + order.getStatus()
		        );
		    }
		
		 
		 if (user.getRole() == Role.ADMIN || order.getUser().getId().equals(userId) ) {
			
			 
			 	
			 	
//			 	order.setDeleted(true);
//				order.setDeletedAt(LocalDateTime.now());
			 	orderRepository.delete(order);
			
				orderRepository.save(order);
			 
				 auditService.log(
			                userId,
			                user.getEmail(),
			                user.getRole().name(),
			                "DELETE_ORDER",
			                "ORDER",
			                orderId,
			                "SUCCESS",
			                "Order soft-deleted successfully"
			        );
			 
		}
		 

			
			else {
				throw new UnauthorizedAccessException( "You are not allowed to delete this order");
			}
			
		}











		
		
	}
	
	
//	private OrderResponse mapToResponse(Order existingOrder) {
//		
//		OrderResponse response = new OrderResponse();
//		response.setId(existingOrder.getId());
//		response.setTotalAmount(existingOrder.getTotalAmount());
//		response.setStatus(existingOrder.getStatus());
//		response.setShippingAddress(existingOrder.getShippingAddress());
////		response.setUpdatedAt(existingOrder.getUpdatedAt());
////		response.setCreatedAt(existingOrder.getCreatedAt());
//		
//		List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
//
//		for (OrderItems items : existingOrder.getOrderItems()) {
//
//			OrderItemResponse orderItemResponse = new OrderItemResponse();
//			orderItemResponse.setId(items.getId());
//			orderItemResponse.setProductId(items.getProductId());
//			orderItemResponse.setProductName(items.getProductName());
//			orderItemResponse.setQuantity(items.getQuantity());
//			orderItemResponse.setPriceAtThatTime(items.getPriceAtThatTime());
////			orderItemResponse.setCreatedAt(items.getCreatedAt());
////			orderItemResponse.setUpdatedAt(items.getUpdatedAt());
//			orderItemResponses.add(orderItemResponse);
//
//		}
//		response.setOrderItems(orderItemResponses);
//		
//		
//		
//		return response;
//		
//	}
	
	
	
	
	
	
	

