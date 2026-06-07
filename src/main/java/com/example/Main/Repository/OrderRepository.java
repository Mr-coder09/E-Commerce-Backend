package com.example.Main.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.Order;

import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>{
	
	
		@EntityGraph(attributePaths = {"orderItems"  ,"orderItems.product" } )
		Page<Order> findByUser_Id(Long userId, Pageable pageable);
		
		@EntityGraph(attributePaths = {"orderItems"  ,"orderItems.product" } )
		Optional<Order> findWithItemsById(long id);
		
		
		Optional<Order> findByUser_IdAndId(Long user_Id , long Id );
		
}
