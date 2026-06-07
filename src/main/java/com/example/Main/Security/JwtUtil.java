package com.example.Main.Security;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "asdjkh2312u9u3218dj12nd129dj129dj192jd12";

	private final long EXPIRATION = 1000 * 60 * 60 * 10;    //10 hrs
//	private final long EXPIRATION = 1000 * 10;
	
	
	public String generateToken(Long id, String email, String role) {
		
	String jti = UUID.randomUUID().toString();
	

	
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", id);
		claims.put("email", email);
		claims.put("role", role);
		

		Date expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
		

//		return Jwts.builder()
//				.setId(jti)
//				.setClaims(claims)
//				.setSubject(email)
//				.setIssuedAt(new Date())
//				.setExpiration(expiryDate)
//				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
//				.compact();

		return Jwts.builder()
			    .setId(jti)
			    .claim("id", id)
			    .claim("email", email)
			    .claim("role", role)
			    .setSubject(email)
			    .setIssuedAt(new Date())
			    .setExpiration(expiryDate)
			    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
			    .compact();
		
		
		
	}

	private Claims extractAllClaims(String token) {
		
		
//		Claims claims = Jwts.parserBuilder()
//				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
//				.build()
//				.parseClaimsJws(token)
//				.getBody();
		
		
		
		Claims claims = Jwts.parserBuilder()
			    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
			    .build()
			    .parseClaimsJws(token)
			    .getBody();
				
			System.out.println("ALL CLAIMS = " + claims);
			System.out.println("JTI = " + claims.getId());

		return claims;

	}

	public String extractEmail(String token) {
		return extractAllClaims(token).getSubject();
	}

	public  Long extractUsedId(String token) {
		
		return  extractAllClaims(token).get("id",Long.class);
//		return  Long.parseLong(id);
	} 

	public  String extractRole(String token) {
		
		return extractAllClaims(token).get("role" ,String.class);
		 
	} 
	
	
	public String extractJti(String token) {
		
		
		return extractAllClaims(token).getId();
		
	}
	
	public Date extractExpiration(String token) {
		
//			Date expiration = extractAllClaims(token).getExpiration();
//			
//		return	expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		return extractAllClaims(token).getExpiration();
		
	}
	
	public boolean validateToken(String token) {
		
		try {
			Claims claims = extractAllClaims(token);
			return !claims.getExpiration().before(new Date());
//			return !extractAllClaims(token).getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
		
		
		
	}
	
}
