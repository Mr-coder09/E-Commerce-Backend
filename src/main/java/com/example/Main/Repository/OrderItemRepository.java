package com.example.Main.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.OrderItems;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long>{

}
