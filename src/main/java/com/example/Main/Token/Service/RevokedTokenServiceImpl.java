//package com.example.Main.Token.Service;
//
//import java.time.LocalDateTime;
//
//import java.time.ZoneId;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.Main.Security.JwtUtil;
//import com.example.Main.Token.Entity.RevokedToken;
//import com.example.Main.Token.Repository.RevokedTokenRepository;
//
//@Service
//public class RevokedTokenServiceImpl  implements RevokedTokenService{
//
//	@Autowired
//	JwtUtil jwtUtil;
//	
//	
//	@Autowired
//	RevokedTokenRepository tokenRepository;
//	
//	
//	
//	
//	@Override
//	public void logout(String token) {
//		
//		
//		String jti = jwtUtil.extractJti(token);
////		LocalDateTime expiration =  jwtUtil.extractExpiration(token);
//		
//		
//		Date exp = jwtUtil.extractExpiration(token);
//		LocalDateTime expiration =
//		        exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		 
//		RevokedToken revokedToken =  new RevokedToken();
//		revokedToken.setJti(jti);
//		revokedToken.setRevokedAt(LocalDateTime.now());
//		revokedToken.setExpiresAt(expiration);
//		tokenRepository.save(revokedToken);
//		
//	}
//
//}
