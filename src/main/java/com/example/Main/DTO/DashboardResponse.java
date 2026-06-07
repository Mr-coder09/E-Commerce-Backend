package com.example.Main.DTO;


import lombok.Data;

@Data
public class DashboardResponse<T> {


	private AdminOrderSummary orderSummary;
    private AdminPaymentSummary paymentSummary;
}
