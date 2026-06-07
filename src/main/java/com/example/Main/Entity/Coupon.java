package com.example.Main.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.Main.Enum.CouponStatus;
import com.example.Main.Enum.DiscountType;
import com.example.Main.HelperClass.BaseSoftDeleteEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Table(
		 indexes = { @Index(name = "idx_coupon_code", columnList = "CouponCode"),
				 @Index(name = "idx_coupon_status", columnList = "status"),
				 @Index(name = "idx_coupon_not_deleted", columnList = "is_deleted"),
				 @Index(name = "idx_coupon_validity", columnList = "valid_from, valid_till")
		
		 })
@SQLDelete(sql = " UPDATE Coupon SET is_deleted = true  WHERE id =? ")



@EntityListeners(AuditingEntityListener.class)
@Data
public class Coupon extends BaseSoftDeleteEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Column(nullable = false, unique = true)
	private String couponCode;
	
	
	@Column(nullable = false)
	private BigDecimal discountValue;
	
	
	private BigDecimal maxDiscountedAmount;
	
	@Column(nullable = false)
	private BigDecimal minOrderAmount;
	
	
	 @Version
	 private Long version; 
	
	@Column(nullable = false)
	private LocalDateTime validFrom;
	
	@Column(nullable = false)
	private LocalDateTime validTill;
	
	@Column(nullable = false)
	private long maxUsage;
	
	@Column(nullable = false)
	private long totalUsed;
	
	@Column(nullable = false)
	private int maxUsagePerUser;
	
	@Enumerated(EnumType.STRING)
	private DiscountType discountType;
	
	@Enumerated(EnumType.STRING)
	private CouponStatus status;
	
	
	
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedBy
	private String updatedBy;
	
	
	
	
	
}
