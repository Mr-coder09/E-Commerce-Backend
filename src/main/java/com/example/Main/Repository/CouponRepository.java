package com.example.Main.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

		Optional<Coupon>findByCouponCode(String code);
			
		boolean existsByCouponCode(String code );
		
		Page<Coupon> findAll(Pageable pageable );
	
}
	