package com.example.Main.Service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.PaymentResponse;
import com.example.Main.Entity.Coupon;
import com.example.Main.Entity.CouponUsage;
import com.example.Main.Entity.Order;
import com.example.Main.Entity.OrderItems;
import com.example.Main.Entity.Payment;
import com.example.Main.Entity.Product;
import com.example.Main.Enum.OrderStatus;
import com.example.Main.Enum.PaymentMethod;
import com.example.Main.Enum.PaymentStatus;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Mapper.PaymentMapper;
import com.example.Main.Repository.CouponRepository;
import com.example.Main.Repository.CouponUagesRepository;
import com.example.Main.Repository.OrderRepository;
import com.example.Main.Repository.PaymentRepository;
import com.example.Main.Repository.ProductRepository;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Service.AuditService;

import com.example.Main.Service.PaymentService;

import jakarta.persistence.OrderBy;



@Service
public class PaymentServiceImpl implements PaymentService{
	
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CouponRepository couponRepository;
	@Autowired
	CouponUagesRepository couponUagesRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	
	
	@Autowired
	PaymentMapper paymentMapper;
	
	@Autowired
	AuditService auditService;
	
	

	@Transactional
	@Override
	public void createPayment(Long orderId,  String paymentMethod) {
		
		Order order = orderRepository.findById(orderId)
				.orElseThrow( () -> new IdNotFoundException("Order Not Found "));
		
//		
//		User user = userRepository.findById(userId)
//				.orElseThrow( () -> new IdNotFoundException("User Not Found "));
		
	
			if (order.getStatus() != OrderStatus.CREATED) {
			
				throw new BadRequestException("   Payment not allowed for order status: " + order.getStatus() );
			
			}
		
		if (paymentRepository.existsByOrder_id(orderId)) {
			throw new BadRequestException(" Payment already initiated for this order");
		}
		
		
		
		
		
		
		
		Payment payment = new Payment();
		
		payment.setOrder(order);
		payment.setUser(order.getUser());
		
		
//		
//		if (order.getCouponCode() == null || order.getCouponCode().isBlank()) {
//		    payment.setAmount(order.getTotalAmount());
//		} 
//		else {
//		    payment.setAmount(order.getFinalAmount());
//		}
//		
		BigDecimal payableAmount =
		        order.getFinalAmount() != null
		                ? order.getFinalAmount()
		                : order.getTotalAmount();

		payment.setAmount(payableAmount);
		
		
		
		
//		order.setStatus(OrderStatus.CREATED);
//		orderRepository.save(order);
		
		
		
		payment.setCurrency("INR");
		payment.setStatus(PaymentStatus.INITIATED);
		
		
		PaymentMethod method;
		try {
		    method = PaymentMethod.valueOf(paymentMethod);
		} catch (IllegalArgumentException ex) {
		    throw new BadRequestException("Invalid payment method");
		}

		payment.setMethod(method);
		
		
		
		
		payment.setGatewayOrderId(UUID.randomUUID().toString());
		payment.setTransactionId(System.currentTimeMillis());

		paymentRepository.save(payment);
		
		
		auditService.log(
				order.getUser().getId(),
				order.getUser().getEmail(),
				order.getUser().getRole().name(),
			    "PAYMENT_CREATED",
			    "PAYMENT",
			    payment.getId(),
			    "SUCCESS",
			    "Payment Created successfully"
			);
		
		
		
		
	}

	@Transactional
	@Override
	public void markPaymentSuccess(Long paymentId, String gatewayPaymentId) {
		
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow( () -> new IdNotFoundException("Payment Id Not Found "));
		
//		if (payment.getStatus() == PaymentStatus.SUCCESS) {
//			return;
//		}
		
		if (payment.getStatus() != PaymentStatus.INITIATED) {
		    throw new BadRequestException(
		        "Cannot mark payment SUCCESS from status: " + payment.getStatus()
		    );
		}
		
		Order order = payment.getOrder();
		
		if (order.getStatus() != OrderStatus.CREATED ) {
			throw new BadRequestException(
		            "Order is not eligible for payment success. Current status: " 
		            + order.getStatus()
		        );
		}
		
		
		
		
		
		payment.setStatus(PaymentStatus.SUCCESS);
		payment.setGatewayPaymentId(gatewayPaymentId);
		payment.setUpdatedAt(LocalDateTime.now());
		

		
		paymentRepository.save(payment);
		
		
		for (OrderItems item : order.getOrderItems()) {
		    Product product = item.getProduct();
		    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
		    productRepository.save(product);
			
			}
		
		
		
		order.setStatus(OrderStatus.PAID);
		orderRepository.save(order);
		

		
		
		if (order.getCouponCode() !=null && !order.getCouponCode().isBlank() ) {
			
			
			Coupon coupon = couponRepository.findByCouponCode(order.getCouponCode())
		            .orElseThrow(() -> new IdNotFoundException("Coupon not found"));
			
			coupon.setTotalUsed(coupon.getTotalUsed()+1);
			couponRepository.save(coupon);
			
			
			CouponUsage usage = couponUagesRepository.findByUserIdAndCouponId(order.getUser().getId() , coupon.getId())
					.orElseGet( () -> {
						CouponUsage us = new CouponUsage();
						us.setCoupon(coupon);
						us.setUser(order.getUser());
						us.setUsageCount(0);
						return us;
					});
			
			usage.setUsageCount(usage.getUsageCount()+1);
			couponUagesRepository.save(usage);
			
		}
		
		
		
		
		
		auditService.log(
				order.getUser().getId(),
				order.getUser().getEmail(),
				order.getUser().getRole().name(),
			    "PAYMENT_markPaymentSuccess",
			    "PAYMENT",
			    payment.getId(),
			    "SUCCESS",
			    "Payment Completed successfully"
			);
		
		
		
		
	}

	
	@Transactional
	@Override
	public void markPaymentFailed(Long paymentId, String failureReason) {
		
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow( () -> new IdNotFoundException("Payment Id Not Found "));
		
		 if (payment.getStatus() != PaymentStatus.INITIATED) {
		        throw new BadRequestException(
		            "Only INITIATED payments can be marked FAILED. Current status: "
		            + payment.getStatus()
		        );
		    }
		
		
		payment.setStatus(PaymentStatus.FAILED);
		 payment.setUpdatedAt(LocalDateTime.now());
		 paymentRepository.save(payment);
		
		 auditService.log(
				    payment.getOrder().getUser().getId(),
				    payment.getOrder().getUser().getEmail(),
				    payment.getOrder().getUser().getRole().name(),
				    "PAYMENT_FAILED",
				    "PAYMENT",
				    payment.getId(),
				    "FAILED",
				    failureReason
				);
		
		
		
	}

	@Override
	public PaymentResponse getPaymentByOrderId(Long orderId) {
		
		  Payment payment =  paymentRepository.findByOrder_Id(orderId)
					.orElseThrow( () -> new IdNotFoundException("Order Id Not Found "));
		  
		  PaymentResponse  response = paymentMapper.toResponse(payment);
		  
		  
		  
//		  response.setId(payment.getId());
//		  response.setOrderId(payment.getOrder().getId());
//		  response.setAmount(payment.getAmount());
//		  response.setCurrency(payment.getCurrency());
//		  response.setStatus(payment.getStatus());
//		  response.setMethod(payment.getMethod());
//		  response.setGatewayOrderId(payment.getGatewayOrderId());
//		  response.setGatewayPaymentId(payment.getGatewayPaymentId());
//		  response.setTransactionId(payment.getTransactionId());
//		  response.setUpdatedAt(payment.getUpdatedAt());
//		  response.setCreatedAt(payment.getCreatedAt());
		  
		  
		
		return response;
	}

	
	@Transactional
	@Override
	public void refundPayment(Long paymentId, BigDecimal refundAmount) {
		
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow( () -> new IdNotFoundException("Payment Id Not Found "));
		
		
		if (payment.getStatus() != PaymentStatus.SUCCESS) {
			throw new BadRequestException(
		            "Only Success payments can be marked Refund . Current status: "
		            + payment.getStatus()
		        );
		}
		
		
		if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
		    throw new BadRequestException("Refund amount must be greater than zero");
		}

		if (refundAmount.compareTo(payment.getAmount()) > 0) {
		    throw new BadRequestException(
		        "Refund amount exceeds paid amount: " + payment.getAmount()
		    );
		}
		
		
		
		
		
		
//		if (refundAmount >=  payment.getAmount()) {
//			throw new BadRequestException(
//		            "Refund Amount is greater then Paid amount . Paid Amount : "
//		            + payment.getAmount()
//		        );
//			
//
//			
//		}
		
		payment.setStatus(PaymentStatus.REFUNDED);
		payment.getOrder().setStatus(OrderStatus.REFUNDED); ;
		paymentRepository.save(payment);
		auditService.log(
			    payment.getOrder().getUser().getId(),
			    payment.getOrder().getUser().getEmail(),
			    payment.getOrder().getUser().getRole().name(),
			    "PAYMENT_REFUND",
			    "PAYMENT",
			    payment.getId(),
			    "SUCCESS",
			    "Refund processed"
			);
		
		
	}
			

}
