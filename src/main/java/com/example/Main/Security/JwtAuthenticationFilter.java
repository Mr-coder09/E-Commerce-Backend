package com.example.Main.Security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Main.Security.RedisService.RedisService;
//import com.example.Main.Service.Impl.OrderServiceImpl;
import com.example.Main.Token.Repository.RevokedTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	RevokedTokenRepository tokenRepository;
	
	@Autowired
	RedisService redisService;

			
	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);		
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		
		
		String token = authHeader.substring(7);
		
		
		
		
		if (!jwtUtil.validateToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid or expired token");
			log.warn("Invalid or expired JWT token");
			return ;
		}
		
		
		
		
		
		String jti = jwtUtil.extractJti(token);
		
		System.out.println("jti for jwt filter ---------"+ jti );
		
//		if (tokenRepository.existsByJti(jti)) {
//			 log.warn("Revoked JWT used: {}", jti);
//			 
//			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	         response.getWriter().write("Token has been revoked");
//	         return ;
//		}
		
		
		if (redisService.isBlacklisted(jti)) {
			
			 log.warn("Revoked JWT used (Redis): {}", jti);
			 
			 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			 response.getWriter().write("Token has been revoked");
			 return ;
		}
		
		
		
		
		
			String  email = jwtUtil.extractEmail(token);
			Long  id = jwtUtil.extractUsedId(token);
			String  role = jwtUtil.extractRole(token);
			
			
			
			
			
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(email,null, List.of(authority));
			
			

			
			SecurityContextHolder.getContext().setAuthentication(authentication);;
			
			request.setAttribute("email", email);
			request.setAttribute("role", role);
			request.setAttribute("userId", id);

			
			
			
			
			
			
			filterChain.doFilter(request, response);
}
	

	
//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) {
//	    return request.getServletPath().startsWith("/api/v1/auth/");
//	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

	    String path = request.getServletPath();

	    return path.startsWith("/api/v1/auth/")
	        || path.startsWith("/api/v1/products");
	}
	
}
