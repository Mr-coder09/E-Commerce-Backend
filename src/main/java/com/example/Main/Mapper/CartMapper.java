package com.example.Main.Mapper;



import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Main.DTO.CartItemsResponse;
import com.example.Main.DTO.CartResponse;
import com.example.Main.Entity.Cart;
import com.example.Main.Entity.CartItems;

@Mapper(componentModel = "spring"
		)
public interface CartMapper {


    @Mapping(target = "cartItems", source = "cartItems")
    CartResponse toResponse(Cart cart);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    CartItemsResponse toItemResponse(CartItems item);
    
    
    
    List<CartItemsResponse> toItemResponseList(List<CartItems> items);
   
}
