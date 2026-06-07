package com.example.Main.Auth;

import com.example.Main.DTO.UserLoginRequest;
import com.example.Main.DTO.UserLoginResponse;
import com.example.Main.Token.Dto.RefreshTokenRequest;

public interface AuthService {

	
	void logout(String token);
	UserLoginResponse loginUser(UserLoginRequest userLoginRequest);
	UserLoginResponse refreshAccessToken(RefreshTokenRequest refreshToken);
}
