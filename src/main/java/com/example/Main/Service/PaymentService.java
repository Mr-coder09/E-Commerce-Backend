package com.example.Main.Service;

import java.math.BigDecimal;

import com.example.Main.DTO.PaymentResponse;

public interface PaymentService {

	
	// 1️⃣ Create payment when order is placed
    void createPayment(Long orderId, String paymentMethod);

    // 2️⃣ Mark payment as success (gateway callback / confirmation)
    void markPaymentSuccess(Long paymentId, String gatewayPaymentId);

    // 3️⃣ Mark payment as failed
    void markPaymentFailed(Long paymentId, String failureReason);

//     4️⃣ Get payment details by order
    PaymentResponse getPaymentByOrderId(Long orderId);
//
//    // 5️⃣ Refund payment (full or partial – future ready)
//    void refundPayment(Long paymentId, BigDecimal refundAmount);
    void refundPayment(Long paymentId, BigDecimal refundAmount);
    
    

}
