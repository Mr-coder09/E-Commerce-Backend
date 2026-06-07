package com.example.Main.Service.Impl;

//import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Main.DTO.AdminOrderDetail;
import com.example.Main.DTO.AdminOrderItem;
import com.example.Main.DTO.AdminOrderRow;
import com.example.Main.DTO.AdminOrderSummary;
import com.example.Main.Entity.Order;
import com.example.Main.Repository.AdminOrderDashboard;
import com.example.Main.Repository.OrderRepository;
import com.example.Main.Service.AdminOrderService;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {
	
	
	@Autowired
    private AdminOrderDashboard adminOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

	@Override
	public Page<AdminOrderRow> getOrders(Pageable pageable) {

		
		return adminOrderRepository.findAllOrdersForAdmin(pageable);
	}

	@Override
	public AdminOrderDetail getOrderDetail(Long orderId) {
		// TODO Auto-generated method stub
		
		Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
		
		 AdminOrderDetail detail = new AdminOrderDetail();
		 detail.setOrderId(order.getId());
	        detail.setUserId(order.getUser().getId());
	       
	        detail.setOrderStatus(order.getStatus());
	        detail.setTotalAmount(order.getTotalAmount());
	        detail.setDiscountedAmount(order.getDiscountedAmount());
	        detail.setFinalAmount(order.getFinalAmount());
	        detail.setCouponCode(order.getCouponCode());
	        detail.setCreatedAt(order.getCreatedAt());
	        detail.setUpdatedAt(order.getUpdatedAt());
	        detail.setDeleted(order.isDeleted());
		
	        detail.setOrderItems(   
	        		order.getOrderItems().stream()
	        		.map( i -> new AdminOrderItem(
	        				i.getProduct().getId(),
	        				i.getProduct().getName(),
	        				i.getQuantity(),
	        				i.getPriceAtThatTime()
	        				
	        				)).collect(Collectors.toList())
	        		);
		
		
		return detail;
	}

	@Override
	public AdminOrderSummary getOrderSummary() {
		// TODO Auto-generated method stub
		return adminOrderRepository.getOrderSummary();
	}

}
