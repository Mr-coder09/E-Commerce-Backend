package com.example.Main.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.Main.DTO.AdminOrderDetail;
import com.example.Main.DTO.AdminOrderRow;
import com.example.Main.DTO.AdminOrderSummary;

public interface AdminOrderService {

	Page<AdminOrderRow> getOrders(Pageable pageable);
    AdminOrderDetail getOrderDetail(Long orderId);
    AdminOrderSummary getOrderSummary();
}
