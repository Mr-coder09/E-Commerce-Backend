package com.example.Main.Service;

import com.example.Main.DTO.AdminCouponRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCouponService {
    Page<AdminCouponRow> getCoupons(Pageable pageable);
}