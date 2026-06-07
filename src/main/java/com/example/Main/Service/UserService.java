package com.example.Main.Service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.example.Main.DTO.UserLoginRequest;
import com.example.Main.DTO.UserLoginResponse;
import com.example.Main.DTO.UserRegisterRequest;
import com.example.Main.DTO.UserResponse;


public interface UserService {



	 
	 public UserResponse regsiterUser(UserRegisterRequest userRegisterRequest);
	 
//	 public UserLoginResponse loginUser(UserLoginRequest userLoginRequest);
	 
	 @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	 public UserResponse getMyProfile(String Email);
	 
	 
	
	 public UserResponse updateMyProfile(UserRegisterRequest userRegisterRequest , Long id,String email);
	 
//	 @PreAuthorize("hasRole('ADMIN')")
//	 public List<UserResponse> getAllProfiles(String email);
	 
	 
	 public void delteProfile(Long id,String email);
	
	
	
}
