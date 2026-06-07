package com.example.Main.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class ProductRequest {
	@NotBlank(message = "name is required")
	@Size(min=2, max=100)
	private String name;
	@Size(max=500)
	private String description;
	@NotNull(message = "price is required")
	@DecimalMin("0.01")
	private BigDecimal price;
	@NotNull(message = "qty  is required")
	@Min(0)
	private int stockQuantity;
	@NotNull(message = "status is required")
	private String status;
	@NotNull(message = "categoryId is required")
	private Long categoryId;

}
