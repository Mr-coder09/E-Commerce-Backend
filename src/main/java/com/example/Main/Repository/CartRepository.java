package com.example.Main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.Cart;
import com.example.Main.Enum.CartStatus;

public interface CartRepository  extends JpaRepository<Cart, Long>{
	
//	Optional<Cart>  findByUserIdAndStatus(Long userId,CardStatus cardStatus);
	Optional<Cart> findByUser_IdAndStatus(Long userId, CartStatus cardStatus);


}
