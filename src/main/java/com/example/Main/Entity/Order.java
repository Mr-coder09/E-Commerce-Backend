package com.example.Main.Entity;




import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
//import org.hibernate.annotations.Index;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;

import com.example.Main.Enum.OrderStatus;
import com.example.Main.HelperClass.BaseSoftDeleteEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Index;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Entity

@Table(
		name = "Orders" ,
		indexes = {
				
				@Index(name =" idx_order_user_id" , columnList = "user_id") ,
				@Index(name = "idx_order_status" , columnList = "status"),
				@Index(name="idx_order_Not_Deleted" , columnList = "is_deleted"),
				@Index(name="idx_order_created_at", columnList = "created_at")
				
		} )


@SQLDelete(sql = " UPDATE Orders SET is_deleted = true  WHERE id =? ")




@Data
@EntityListeners(AuditingEntityListener.class)
public class Order  extends BaseSoftDeleteEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private BigDecimal totalAmount;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private String shippingAddress ;
	
	@OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL ,fetch = FetchType.LAZY )
	private List<OrderItems> orderItems;
	
	
	private  BigDecimal discountedAmount;
	
	private  BigDecimal finalAmount;
	

	private String couponCode;

	 @Version
	  private Long version; 
	
	
	
	
	
	
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
