//package com.example.Main.Token.Controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.Main.Token.Service.RevokedTokenService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//
//
//@RestController
//@RequestMapping("/api/v1")
//public class Logout {
//	
//	@Autowired
//	RevokedTokenService authService;
//	
//
//	@PostMapping("/auth/logout")
//	public ResponseEntity<Void> logout( @RequestHeader("Authorization") String authHeader){
//		
//		
//		String token = authHeader.substring(7);
//		
//		
//		 authService.logout(token);
//		
//		return ResponseEntity.noContent().build();
//		
//	}
//	
//	
//}
