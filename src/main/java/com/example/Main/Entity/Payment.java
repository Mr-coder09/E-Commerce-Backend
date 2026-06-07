package com.example.Main.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import com.example.Main.Enum.PaymentMethod;
import com.example.Main.Enum.PaymentStatus;
import com.example.Main.HelperClass.BaseSoftDeleteEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)

@Data
@Table(
		name = "payments" ,
		indexes = {
				
				@Index(name ="idx_payment_order_id " , columnList = "order_id") ,
				@Index(name = "idx_payment_user_id" , columnList = "user_id")
				
		}
					
					

				
	
)
@SQLDelete(sql = " UPDATE payments SET is_deleted = true  WHERE id =? ")

public class Payment   extends BaseSoftDeleteEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne(optional = false , fetch = FetchType.LAZY )
	@JoinColumn(name = "order_id", nullable = false, unique = true)
	private Order order;
	
	@ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	private User user;
	
    @Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;
	
    @Column(length = 3, nullable = false)
	private String Currency ;
	
    
    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod method;
	
	 @Version
	 private Long version; 
	
	
	
	private String gatewayPaymentId;
	
	private String gatewayOrderId;
	
	private long transactionId;
	
	@CreatedBy
	@Column(updatable = false)
	private String createdBy;

	@LastModifiedBy
	private String updatedBy;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;
	

}
