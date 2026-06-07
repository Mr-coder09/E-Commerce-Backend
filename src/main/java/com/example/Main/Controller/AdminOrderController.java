package com.example.Main.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.AdminOrderDetail;
import com.example.Main.DTO.AdminOrderRow;
import com.example.Main.DTO.AdminOrderSummary;
import com.example.Main.Service.AdminOrderService;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin/orders")
public class AdminOrderController {
	
	
	 @Autowired
	    private AdminOrderService adminOrderService;
	 
	 
	 @GetMapping
	 public ResponseEntity<Page<AdminOrderRow>> getOrders( @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size){
		 
		 
		return ResponseEntity.ok(  adminOrderService.getOrders(PageRequest.of(page, size , Sort.by("createdAt").descending())));
		 
		 
	 }
		 
	 @GetMapping("/{orderId}")
	    public ResponseEntity<AdminOrderDetail> getOrderDetail(@PathVariable Long orderId) {
	        return ResponseEntity.ok(adminOrderService.getOrderDetail(orderId));
	    }

	    @GetMapping("/summary")
	    public ResponseEntity<AdminOrderSummary> getOrderSummary() {
	        return ResponseEntity.ok(adminOrderService.getOrderSummary());
	    }
	 
	 
		 
}
