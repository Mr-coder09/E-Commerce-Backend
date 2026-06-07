package com.example.Main.DTO;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
	
	@NotBlank(message = "name is required")
	@Size(min = 2,max = 10)
	private String name;
	
	@Size(max=255)
	private String description;



}
