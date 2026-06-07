package com.example.Main.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.PaymentResponse;
import com.example.Main.Service.PaymentService;



@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;

	@PostMapping("/initiate")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Void> createPayment(@RequestParam Long orderId,
            @RequestParam String paymentMethod ){
		
		   paymentService.createPayment(orderId, paymentMethod);
		
		return ResponseEntity.noContent().build();
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{paymentId}/success")
    public ResponseEntity<Void> markPaymentSuccess(@PathVariable Long paymentId , @RequestParam  String gatewayPaymentId ){
		
    	paymentService.markPaymentSuccess(paymentId, gatewayPaymentId);
    	
    	return ResponseEntity.noContent().build();
    	
    	
    	
    }
	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{paymentId}/failed")
    public ResponseEntity<Void> markPaymentFailed(@PathVariable Long paymentId , @RequestParam String failureReason ){
    	
    	paymentService.markPaymentFailed(paymentId, failureReason);
    	
    	return ResponseEntity.noContent().build();
    	
    	
    	
    }
	
	@PreAuthorize("hasRole('ADMIN')  or hasRole('CUSTOMER') " )
    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId ){
    	
    	PaymentResponse response = paymentService.getPaymentByOrderId(orderId);
    	return ResponseEntity.ok().body(response);
    	
    	
    	
    }
	
	@PostMapping("/refund")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> refundPayment(
			@RequestParam long paymentId ,
			@RequestParam BigDecimal refundAmount
			){
		
		paymentService.refundPayment(paymentId, refundAmount);
		
				return ResponseEntity.noContent().build();
		
		
		
	}
	
	
}
