package com.example.Main.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Main.DTO.OrderItemResponse;
import com.example.Main.DTO.OrderResponse;
import com.example.Main.Entity.Order;
import com.example.Main.Entity.OrderItems;


@Mapper(
		componentModel = "spring" 
		
		)
public interface OrderMapper {
	
	
	
	OrderResponse toResponse(Order order);
	
	@Mapping(target = "productId" , source = "product.id")
	@Mapping(target = "productName" ,source = "product.name")
	OrderItemResponse toOrderItemResponse (OrderItems orderItems);
	
	
	List<OrderItemResponse> toOrderItemResponses (List<OrderItems> orderItems);

}
