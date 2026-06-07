package com.example.Main.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Main.DTO.AdminOrderRow;
import com.example.Main.DTO.AdminOrderSummary;
import com.example.Main.Entity.Order;





public interface AdminOrderDashboard extends JpaRepository<Order, Long>{
	
	
	
	
	


	
	   @Query("""
		        SELECT NEW com.example.Main.DTO.AdminOrderRow(
		            o.id,
		            u.id,
		            u.email,
		            o.status,
		            o.totalAmount,
		            o.discountedAmount,
		            o.couponCode,
		            p.status,
		            o.createdAt,
		            o.isDeleted
		        )
		        FROM Order o
		        JOIN user u ON o.user = u
		        LEFT JOIN Payment p ON p.order = o
		        
		    """)
	 Page<AdminOrderRow> findAllOrdersForAdmin(Pageable pageable );
	

	 
	 @Query("""
			    SELECT NEW com.example.Main.DTO.AdminOrderSummary(
			        COUNT(o),
			        SUM(CASE WHEN o.status = 'CREATED' THEN 1 ELSE 0 END),
			        SUM(CASE WHEN o.status = 'PAID' THEN 1 ELSE 0 END),
			        SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END),
			        SUM(CASE WHEN o.status = 'REFUNDED' THEN 1 ELSE 0 END),
			        COALESCE(SUM(CASE WHEN o.status = 'PAID' THEN o.finalAmount ELSE 0 END), 0),
			        COALESCE(SUM(
			            CASE
			                WHEN o.status = 'PAID'
			                AND o.createdAt >= CURRENT_DATE
			                THEN o.finalAmount
			                ELSE 0
			            END
			        ), 0)
			    )
			    FROM Order o
			    WHERE o.isDeleted = false
			""")
			AdminOrderSummary getOrderSummary();
	
	

}
