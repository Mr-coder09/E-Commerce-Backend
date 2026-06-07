package com.example.Main.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.SendOtpRequest;
import com.example.Main.DTO.UserLoginRequest;
import com.example.Main.DTO.UserLoginResponse;
import com.example.Main.DTO.VerifyOtpRequest;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Service.OtpService;
import com.example.Main.Service.UserService;
import com.example.Main.Token.Dto.RefreshTokenRequest;
//import com.example.Main.Token.Service.RevokedTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

//	@Autowired
//	UserService userService;
	
//	@Autowired
//	RevokedTokenService authService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	OtpService otpService;
	
	
//	private String getAdminEmail(HttpServletRequest req) {
//		
//		
//		String adminEmail = (String) req.getAttribute("email");
//		if (adminEmail == null) {
//			 throw new UnauthorizedAccessException("Access Denied");
//		}
//		
//		
//		return adminEmail;
//	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest){
		
//		UserResponse response = userService.loginUser(userLoginRequest);
		UserLoginResponse userLoginResponse = authService.loginUser(userLoginRequest);
		
			
			 
			 return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
		
		
	}
	
	
	@PostMapping("/auth/send-otp")
	public ResponseEntity<String> sendOtp(
	    @Valid   @RequestBody SendOtpRequest request) {

	    otpService.sendOtp(request.getEmail());

	    return ResponseEntity.ok(
	            "OTP sent successfully");
	}
	
	
	
	@PostMapping("/auth/verify-otp")
	public ResponseEntity<String> verifyOtp(
	        @RequestBody VerifyOtpRequest request) {

	    String token = otpService.verifyOtp(
	            request.getEmail(),
	            request.getOtp()
	    );

	    return ResponseEntity.ok(token);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<Void> logout( @RequestHeader("Authorization") String authHeader){
		
		
		String token = authHeader.substring(7);
		
		
		 authService.logout(token);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<UserLoginResponse> refreshToken(
	        @RequestBody RefreshTokenRequest request) {

	    UserLoginResponse response =
	            authService.refreshAccessToken(request);

	    return ResponseEntity.ok(response);
	}}
