package com.example.Main.HelperClass;

import java.math.BigDecimal;
import java.util.List;

import com.example.Main.Entity.CartItems;

public class RecalculateTotalAmount {

	
	public static BigDecimal calculateTotalAmount (List<CartItems> items) {
		
		BigDecimal recalulator = BigDecimal.ZERO;
		
		for (CartItems cartItems : items) {
			BigDecimal total = (  cartItems.getPriceAtThatTime().multiply(BigDecimal.valueOf(cartItems.getQuantity())) );
			
			recalulator = recalulator.add(total);
			
		}
		
		
		
		return recalulator;
		
		
		
	}
}
