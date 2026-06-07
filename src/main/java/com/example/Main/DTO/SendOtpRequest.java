package com.example.Main.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequest {
	@NotBlank(message = "Email is required")

    @Email(message = "Invalid email")
	String email;
}
