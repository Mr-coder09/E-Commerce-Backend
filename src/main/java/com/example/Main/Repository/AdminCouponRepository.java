package com.example.Main.Repository;

import com.example.Main.DTO.AdminCouponRow;
import com.example.Main.Entity.Coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminCouponRepository extends JpaRepository<Coupon, Long> {

    @Query("""
        SELECT new com.example.Main.DTO.AdminCouponRow(
            c.id,
            c.couponCode,
            c.discountType,
            c.discountValue,
            c.maxUsage,
            c.totalUsed,
            c.maxUsagePerUser,
            c.status,
            c.validFrom,
            c.validTill
        )
        FROM Coupon c
    """)
    Page<AdminCouponRow> findAllCoupons(Pageable pageable);
}