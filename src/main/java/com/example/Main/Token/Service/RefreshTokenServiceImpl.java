package com.example.Main.Token.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.Entity.User;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Security.JwtUtil;
import com.example.Main.Service.AuditService;
import com.example.Main.Token.Entity.RefreshToken;
import com.example.Main.Token.Entity.RevokedToken;
import com.example.Main.Token.Repository.RefreshTokenRepository;
import com.example.Main.Token.Repository.RevokedTokenRepository;



@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	
	private static final long REFRESH_TOKEN_DAYS = 7;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    RevokedTokenRepository revokedTokenRepository;
    
    
    @Autowired
	AuditService auditService;
     
    private static Logger log =  LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
	
    @Autowired
    UserRepository userRepository;

	@Override
	public RefreshToken createRefreshToken(Long userId) {
		
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUserId(userId);
//		refreshToken.setUserEmail(null);
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setRevoked(false);
		refreshToken.setCreatedAt(LocalDateTime.now());
		refreshToken.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_DAYS));
		refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Transactional
	@Override
	public RefreshToken validateRefreshToken(String token) {
	
		
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
								.orElseThrow(() -> new UnauthorizedAccessException("Invalid refresh token"));
		
		if (refreshToken.isRevoked()) {
			
			 refreshTokenRepository.revokeAllByUserId(refreshToken.getUserId()); 
			 
			 
			RevokedToken revokedToken = new RevokedToken();
			revokedToken.setJti(jwtUtil.extractJti(token));
			revokedToken.setRevokedAt(LocalDateTime.now());
//			revokedToken.setExpiresAt(LocalDateTime.now().plusHours(1));
			
			Date exp = jwtUtil.extractExpiration(token);
			LocalDateTime expiresAt =
			    exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			revokedToken.setExpiresAt(expiresAt);
			revokedTokenRepository.save(revokedToken);
			 
			 
			 
			
			log.warn("REFRESH_TOKEN_REUSE_DETECTED for userId={}", refreshToken.getUserId());
			
			User user = userRepository.findById(refreshToken.getUserId())
				    .orElseThrow(() -> new UnauthorizedAccessException("User not found"));
			
			auditService.log(refreshToken.getUserId(),user.getEmail() , user.getRole().name(),
					"REFRESH_TOKEN_REUSE", "RefreshTokenService", refreshToken.getId(), "FAILURE", "REFRESH_TOKEN_REUSE_DETECTED");
			
			
			throw new UnauthorizedAccessException("Refresh token revoked");
			
		}
		if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			revokeRefreshToken(token);
			throw new UnauthorizedAccessException("Refresh token expired");
		}
		
		return refreshToken;
		
		
		
	}

	@Transactional
	@Override
	public void revokeRefreshToken(String token) {
		
		refreshTokenRepository.findByToken(token).ifPresent( rt -> {
			
			rt.setRevoked(true);
			refreshTokenRepository.save(rt);
			});
		
		
	}

	@Transactional
	@Override
	public void revokeAllForUser(Long userId) {
	
		refreshTokenRepository.revokeAllByUserId(userId);
		
		
	}

}
