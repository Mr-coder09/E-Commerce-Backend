package com.example.Main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.CouponUsage;



public interface CouponUagesRepository extends JpaRepository<CouponUsage, Long> {

//	   CouponUsage findByUserId(User userId);
	Optional<CouponUsage> findByUserIdAndCouponId(Long userId,Long couponId);
						  
}
