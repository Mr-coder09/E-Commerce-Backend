package com.example.Main.Token.Service.Cleanup;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.Token.Repository.RefreshTokenRepository;
import com.example.Main.Token.Repository.RevokedTokenRepository;

@Component
@EnableScheduling
public class TokenCleanupJob {
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	RevokedTokenRepository revokedTokenRepository;

	
	public static final Logger log = LoggerFactory.getLogger(TokenCleanupJob.class);

	@Scheduled(fixedDelay = 60 * 60 * 1000)
	@Transactional
    public void cleanupExpiredRevokedTokens() {
    	
    	log.info("\"Expired tokens cleanup started\"");
    	revokedTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    	
    	log.info("Expired tokens cleanup completed");
    	

    	
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
	@Transactional
    public void cleanupExpiredRefreshTokens() {
    	log.info("\"Expired tokens cleanup started\"");
    	refreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    	log.info("Expired tokens cleanup completed");
    }
	
}
