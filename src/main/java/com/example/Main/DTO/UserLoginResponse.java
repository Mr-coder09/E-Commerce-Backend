package com.example.Main.DTO;

import lombok.Data;

@Data
public class UserLoginResponse {
	
	
	String accessToken;
	String refreshToken;
	UserResponse user;

}
