package com.example.Main.Controller;

import com.example.Main.DTO.DashboardResponse;
//import com.example.Main.DTO.AdminDashboardResponse;
import com.example.Main.Service.AdminOrderService;
import com.example.Main.Service.AdminPaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private AdminOrderService adminOrderService;

    @Autowired
    private AdminPaymentService adminPaymentService;

    @GetMapping
    public ResponseEntity<DashboardResponse> dashboard() {

        DashboardResponse response = new DashboardResponse();
       
        response.setOrderSummary(adminOrderService.getOrderSummary());
        response.setPaymentSummary(adminPaymentService.getPaymentSummary());

        return ResponseEntity.ok(response);
    }
}