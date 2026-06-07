package com.example.Main.Service.Impl;

import com.example.Main.DTO.AdminPaymentRow;
import com.example.Main.DTO.AdminPaymentSummary;
import com.example.Main.Repository.AdminPaymentRepository;
import com.example.Main.Service.AdminPaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminPaymentServiceImpl implements AdminPaymentService {

    @Autowired
    private AdminPaymentRepository adminPaymentRepository;

    @Override
    public Page<AdminPaymentRow> getPayments(Pageable pageable) {
        return adminPaymentRepository.findAllPayments(pageable);
    }

    @Override
    public AdminPaymentSummary getPaymentSummary() {
        return adminPaymentRepository.getPaymentSummary();
    }
}