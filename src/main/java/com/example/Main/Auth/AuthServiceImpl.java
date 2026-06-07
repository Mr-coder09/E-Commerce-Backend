package com.example.Main.Auth;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Main.DTO.UserLoginRequest;
import com.example.Main.DTO.UserLoginResponse;
import com.example.Main.DTO.UserResponse;
import com.example.Main.Entity.User;
import com.example.Main.Exceptions.BadRequestException;
import com.example.Main.Exceptions.IdNotFoundException;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Repository.UserRepository;
import com.example.Main.Security.JwtUtil;
import com.example.Main.Security.RedisService.RedisService;
import com.example.Main.Service.AuditService;
import com.example.Main.Service.CaptchaService;
import com.example.Main.Token.Dto.RefreshTokenRequest;
import com.example.Main.Token.Entity.RefreshToken;
import com.example.Main.Token.Entity.RevokedToken;
import com.example.Main.Token.Repository.RevokedTokenRepository;
import com.example.Main.Token.Service.RefreshTokenService;



@Service
public class AuthServiceImpl implements AuthService{
	
	
	@Autowired
	AuditService auditService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired
	RevokedTokenRepository tokenRepository;
	
	
	@Autowired
	RedisService redisService;
	
//	@Autowired
//	CaptchaService captchaService;
	
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	

	@Transactional
	@Override
	public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {


//		if (!captchaService.validateCaptcha(userLoginRequest.getCaptchaToken())) {
//
//		    throw new BadRequestException("Invalid CAPTCHA");
//		}
//		
		
		
		
		User user = userRepository.findByEmail(userLoginRequest.getEmail())
	            .orElseThrow(() -> {
	                log.warn("Login failed: email not found [{}]", userLoginRequest.getEmail());
	                auditService.log(
	                	    null,
	                	    userLoginRequest.getEmail(),
	                	    null,
	                	    "LOGIN",
	                	    "USER",
	                	    null,
	                	    "FAILED",
	                	    "Email not found"
	                	);
	                return new UnauthorizedAccessException("Invalid email");
	            });
		
		
		if (user.isDeleted()) {
			
			auditService.log(
				    user.getId(),
				    user.getEmail(),
				    user.getRole().name(),
				    "LOGIN",
				    "USER",
				    user.getId(),
				    "FAILED",
				    "Invalid email or password"
				);
			
			
			
			throw new UnauthorizedAccessException("Invalid email or password");
			
			
			
			
		}

		if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
			
			
			log.warn("Login failed: invalid password for email [{}]", userLoginRequest.getEmail());
			
			auditService.log(
				    user.getId(),
				    user.getEmail(),
				    user.getRole().name(),
				    "LOGIN",
				    "USER",
				    user.getId(),
				    "FAILED",
				    "Invalid password"
				);
			
//			throw new RuntimeException("Invalid Password ");
			throw new UnauthorizedAccessException("Invalid email or password");
		}

		
		Long id = user.getId();
		String email=user.getEmail();
		String role= user.getRole().name();
		
		String accessToken = jwtUtil.generateToken(id, email, role);
		RefreshToken rt = refreshTokenService.createRefreshToken(id);
		
		String refreshToken = rt.getToken();
		
//		UserResponse response = new UserResponse();
//		response.setName(user.getName());
//		response.setEmail(user.getEmail());
//		response.setGender(user.getGender());
//		response.setMobileNo(user.getMobileNo());
		
		UserResponse response = mapToResponse(user);
		
		UserLoginResponse userLoginResponse = new UserLoginResponse();
		userLoginResponse.setAccessToken(accessToken);
		userLoginResponse.setRefreshToken(refreshToken);
		userLoginResponse.setUser(response);
		
		
		log.info("User login successful: email={}", user.getEmail());
		
		auditService.log(
			    user.getId(),
			    user.getEmail(),
			    user.getRole().name(),
			    "LOGIN",
			    "USER",
			    user.getId(),
			    "SUCCESS",
			    "User logged in successfully"
			);

		return userLoginResponse;
	}

	
	private UserResponse mapToResponse(User user) {
		
		UserResponse response = new UserResponse();

		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setGender(user.getGender());
		response.setMobileNo(user.getMobileNo());
		response.setId(user.getId());
		return response;
		
		
	}



	@Transactional
	@Override
	public UserLoginResponse refreshAccessToken(RefreshTokenRequest oldRefreshToken) {
		
		
		
	     RefreshToken existingRefreshToken = 
	    		 refreshTokenService.validateRefreshToken(oldRefreshToken.getRefreshToken());
	     
	     long userId = existingRefreshToken.getUserId();
	     
	     User user = userRepository.findById(userId)
	    		 .orElseThrow(() -> new IdNotFoundException("User not found"));;
	    		 
	    		 
	    String accessToken =  jwtUtil.generateToken(userId, user.getEmail(), user.getRole().name());
		
	    
	    refreshTokenService.revokeRefreshToken(oldRefreshToken.getRefreshToken());

	    RefreshToken rt = refreshTokenService.createRefreshToken(userId);
	    String refreshToken = rt.getToken();
		
	    UserResponse response = mapToResponse(user);
	    
	    UserLoginResponse userLoginResponse = new UserLoginResponse();
	    userLoginResponse.setAccessToken(accessToken);
	    userLoginResponse.setRefreshToken(refreshToken);
	    userLoginResponse.setUser(response);
	    
	    
	    auditService.log(
	    	    user.getId(),
	    	    user.getEmail(),
	    	    user.getRole().name(),
	    	    "REFRESH_TOKEN",
	    	    "AUTH",
	    	    user.getId(),
	    	    "SUCCESS",
	    	    "Access token refreshed"
	    	);
	    
	    
		return userLoginResponse;
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	@Transactional
	@Override
	public void logout(String token) {
		
		
		
		  if (!jwtUtil.validateToken(token)) {
		        throw new UnauthorizedAccessException("Invalid or expired token");
		    }

		
		String jti = jwtUtil.extractJti(token);
//		LocalDateTime expiration =  jwtUtil.extractExpiration(token);

		
		
		Date exp = jwtUtil.extractExpiration(token);
		long expiry =  exp.getTime() - System.currentTimeMillis();		
		 

	    if (jti == null) {
	        throw new IllegalStateException("JWT ID (jti) is missing");
	    }
	    
	    
	    System.out.println("Logout token = " + token);
	    System.out.println("Logout jti = " + jwtUtil.extractJti(token));
	    
	    redisService.blacklistToken(jti, expiry);
	    
	    
	    
	    
	    
		
		
//		RevokedToken revokedToken =  new RevokedToken();
//		revokedToken.setJti(jti);
//		revokedToken.setRevokedAt(LocalDateTime.now());
//		revokedToken.setExpiresAt(expiration);
//		tokenRepository.save(revokedToken);
	    
	    
	    
	    
	    
	    
	    
	    
		
		Long userId = jwtUtil.extractUsedId(token);
		String email = jwtUtil.extractEmail(token);
		String role = jwtUtil.extractRole(token);
		
		refreshTokenService.revokeAllForUser(userId);
		
		log.info("Logout Session by user = {}", userId);
		
		auditService.log(userId, email, role, "LOGOUT", "USER", userId, "SUCCESS", "User logged in successfully");
		
		
		
		
	}
	
	
	
	
	
	
	
	

}
