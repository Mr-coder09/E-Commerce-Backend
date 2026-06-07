package com.example.Main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.Main.Entity.CartItems;


public interface CartItemsRepository extends JpaRepository<CartItems, Long>{
	
	
	
	
	Optional<CartItems> findByCartIdAndProductId(Long cartId, Long productId);
//	findByUserIdAndStatus();
	
	

}
