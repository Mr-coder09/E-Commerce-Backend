package com.example.Main.Token.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.Main.Token.Entity.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{

	 Optional<RefreshToken> findByToken(String token);
	 boolean existsByTokenAndRevokedFalse(String token);
	 void deleteByUserId(Long userId);
	 
	 @Modifying
	 @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.userId = :userId")
	 void revokeAllByUserId(Long userId);
	 

	 void deleteByExpiresAtBefore(LocalDateTime time);
	 
}
