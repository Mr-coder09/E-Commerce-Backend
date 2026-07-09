package com.example.Main.Security;

//import org.apache.catalina.filters.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.Main.Security.RateLimit.RateLimitingFilter;

import jakarta.persistence.EntityManager;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

  
	@Autowired
	 private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Autowired
	private RateLimitingFilter rateLimitingFilter;
	
	
//	@Autowired
//	EntityManager entityManager;
//
//	public void enableSoftDeleteFilter() {
//	    entityManager.unwrap(Session.class)
//	        .enableFilter("softDeleteFilter")
//	        .setParameter("isDeleted", false);
//	}
//	
	
	
	
   

	@Bean
	public  SecurityFilterChain securityFilterChain (HttpSecurity http)  throws Exception{
		
	
		
		
		http.csrf(csrf -> csrf.disable())
		
			
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
						    "/actuator/health",
						    "/api/v1/auth/**",
						    "/v3/api-docs/**",
						    "/swagger-ui/**",
						    "/swagger-ui.html",
						    "/api/v1/auth/send-otp",
						    "/api/v1/auth/verify-otp",
						    "/api/v1/products/**"
						).permitAll()
				    .anyRequest().authenticated()
				)
			
			
			
			.sessionManagement(session -> 
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);
		
		

//		http.addFilterBefore(rateLimitingFilter,JwtAuthenticationFilter.class);
//
//		http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
		
//		http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
//		http.addFilterAfter(jwtAuthenticationFilter, RateLimitingFilter.class);
		
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(rateLimitingFilter, JwtAuthenticationFilter.class);
		
		
		
		return http.build();
	}

	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception{
		
		return auth.getAuthenticationManager();
		
		
	}
	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	
		return new BCryptPasswordEncoder();
		
		
	}
	
	
}
