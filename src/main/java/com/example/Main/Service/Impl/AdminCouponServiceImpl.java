package com.example.Main.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Main.DTO.AdminCouponRow;
import com.example.Main.Repository.AdminCouponRepository;
import com.example.Main.Service.AdminCouponService;

@Service
public class AdminCouponServiceImpl implements AdminCouponService {

    @Autowired
    private AdminCouponRepository adminCouponRepository;

    @Override
	public Page<AdminCouponRow> getCoupons(Pageable pageable) {
		 return adminCouponRepository.findAllCoupons(pageable);

	}

//    @Override
//    public Page<AdminCouponRow> getCoupons(Pageable pageable) {
//        return adminCouponRepository.findAllCoupons(pageable);
//    }
}