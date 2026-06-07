package com.example.Main.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
	@NotBlank(message = "name is required")
	@Size(min = 2 , max = 20)
	private String name;
	@Email (message = "Email is required")
	@NotBlank
	private String email;
	@NotBlank(message = "password is required")
	@Size(min=8, max=20)
	private String password;
	@NotNull(message = "gender is required")
	private String gender;
	@Pattern(regexp = "^[0-9]{10}$")
	
	private String mobileNo;
}
