package com.example.Main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
		Optional<User> findByEmail(String email);
		boolean existsByEmail(String email);
		boolean existsByMobileNo(String mobileNo);
	
}
