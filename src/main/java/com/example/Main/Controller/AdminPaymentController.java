package com.example.Main.Controller;

//import com.example.Main.DTO.AdminPaymentRow;
import com.example.Main.DTO.AdminPaymentSummary;
import com.example.Main.Service.AdminPaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    @Autowired
    private AdminPaymentService adminPaymentService;

    @GetMapping
    public ResponseEntity<?> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
            adminPaymentService.getPayments(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
            )
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<AdminPaymentSummary> getSummary() {
        return ResponseEntity.ok(adminPaymentService.getPaymentSummary());
    }
}
