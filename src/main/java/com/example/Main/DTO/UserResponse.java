package com.example.Main.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data

public class UserResponse {


	private Long id;
	private String name;
	private String email;
	private String gender;
	private String mobileNo;
	private LocalDateTime updatedAt;
	
}
