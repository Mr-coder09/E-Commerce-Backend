package com.example.Main.Token.Repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Main.Token.Entity.RevokedToken;



public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long>{
		
			boolean existsByJti(String jti);
			
			void deleteByExpiresAtBefore(LocalDateTime time);
			
	
	
}
