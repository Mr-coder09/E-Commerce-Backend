package com.example.Main.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Main.DTO.AdminPaymentRow;
import com.example.Main.DTO.AdminPaymentSummary;
import com.example.Main.Entity.Payment;

public interface AdminPaymentRepository extends JpaRepository<Payment, Long> {

	 @Query("""
		        SELECT new com.example.Main.DTO.AdminPaymentRow(
		            p.id,
		            p.order.id,
		            p.user.id,
		            p.amount,
		            p.method,
		            p.status,
		            p.gatewayPaymentId,
		            p.createdAt
		        )
		        FROM Payment p
		    """)
		    Page<AdminPaymentRow> findAllPayments(Pageable pageable);

		    @Query("""
		        SELECT new com.example.Main.DTO.AdminPaymentSummary(
		            COUNT(p),
		            SUM(CASE WHEN p.status = 'SUCCESS' THEN 1 ELSE 0 END),
		            SUM(CASE WHEN p.status = 'FAILED' THEN 1 ELSE 0 END),
		            SUM(CASE WHEN p.status = 'REFUNDED' THEN 1 ELSE 0 END),
		            COALESCE(SUM(CASE WHEN p.status = 'SUCCESS' THEN p.amount ELSE 0 END),0),
		            COALESCE(SUM(CASE WHEN p.status = 'REFUNDED' THEN p.amount ELSE 0 END),0)
		        )
		        FROM Payment p
		    """)
		    AdminPaymentSummary getPaymentSummary();
	
}
