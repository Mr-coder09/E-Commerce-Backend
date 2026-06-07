package com.example.Main.DTO;

import lombok.Data;

@Data
public class VerifyOtpRequest {
	private String email;
	private String otp;

}
