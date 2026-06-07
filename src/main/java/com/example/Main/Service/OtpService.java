package com.example.Main.Service;

public interface OtpService {

	
	void sendOtp(String email);

	String verifyOtp(String email, String otp);
}
