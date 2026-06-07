package com.example.Main.Repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.Payment;



public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	 Optional<Payment> findByOrder_Id(Long orderId);
	 
	 
	 boolean existsByOrder_id(Long orderId);
//
//	    boolean existsByTransactionId(String transactionId);
//
//	    boolean existsByGatewayPaymentId(String gatewayPaymentId);
//
//	    long countByStatus(PaymentStatus status);

}
