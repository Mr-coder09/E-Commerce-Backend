package com.example.Main.Service;

import com.example.Main.DTO.AdminPaymentRow;
import com.example.Main.DTO.AdminPaymentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminPaymentService {

    Page<AdminPaymentRow> getPayments(Pageable pageable);
    AdminPaymentSummary getPaymentSummary();
}