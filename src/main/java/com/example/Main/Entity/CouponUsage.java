package com.example.Main.Entity;

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

import com.example.Main.HelperClass.BaseSoftDeleteEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@SQLDelete(sql = "UPDATE CouponUsage  SET is_deleted = true WHERE id = ? ")


@Table(
		 indexes = {
				    @Index(name = "idx_coupon_user", columnList = "user_id,coupon_id")
				  }
		
		)
public class CouponUsage extends BaseSoftDeleteEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
//	@ManyToOne(optional = false ,fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	private User user;
	
	
	 @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "coupon_id", nullable = false)
	

//	@ManyToOne(optional = false ,fetch = FetchType.LAZY)
	private Coupon coupon;
	
	@Column(nullable = false)
	private long usageCount;
	
	

	
	 @Version
	    private Long version; 

	@CreatedDate
	private LocalDateTime creadtedAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedBy
	private String updatedBy;

	
	
}
