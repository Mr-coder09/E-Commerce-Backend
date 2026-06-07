package com.example.Main.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.Main.DTO.UserLoginRequest;
import com.example.Main.DTO.UserLoginResponse;
import com.example.Main.DTO.UserRegisterRequest;
import com.example.Main.DTO.UserResponse;
import com.example.Main.Entity.User;
import com.example.Main.Exceptions.UnauthorizedAccessException;
import com.example.Main.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	@Autowired
	UserService userService;
	
	
	private String getAdminEmail() {
		
		
//		String adminEmail = (String) req.getAttribute("email");
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String adminEmail = auth.getName();
		if (adminEmail == null) {
			 throw new UnauthorizedAccessException("Access Denied");
		}
		
		
		return adminEmail;
	}
	
	
	
	
	@PostMapping("/auth/register")
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {

		 UserResponse userResponse =  userService.regsiterUser(userRegisterRequest);
		 
		 if (userResponse!=null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
		}
		 else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	
	
	
		
	}
	
	
//	
//	@PostMapping("/auth/login")
//	public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest){
//		
////		UserResponse response = userService.loginUser(userLoginRequest);
//		UserLoginResponse userLoginResponse = userService.loginUser(userLoginRequest);
//		
//			
//			 
//			 return ResponseEntity.status(HttpStatus.OK).body(userLoginResponse);
//		
//		
//	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	
	@GetMapping("/users/me")
	public ResponseEntity<UserResponse> getMyProfile() {
		
		String email = getAdminEmail();
		
		
		
		UserResponse response =	userService.getMyProfile(email);
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
		
	}
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/users/{id}")
	public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRegisterRequest userRegisterRequest,
													@PathVariable Long id) {
		
		
		String email = getAdminEmail();
		
		UserResponse response = userService.updateMyProfile(userRegisterRequest,id,email);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
		
		
	}
	
//	@GetMapping("/admin/users")
//	public ResponseEntity<List<UserResponse>> getAllProfile( ) {
//		
//		
//		String email = getAdminEmail();
//		List<UserResponse> userResponses = userService.getAllProfiles(email);
//		return ResponseEntity.ok(userResponses);
//		
//		
//	}
	 @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/admin/users/{id}")
	public ResponseEntity<UserResponse> deleteProfile(@PathVariable Long id) {
//		String email = (String) request.getAttribute("email");
		String email = getAdminEmail();
		userService.delteProfile(id,email);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	
}
