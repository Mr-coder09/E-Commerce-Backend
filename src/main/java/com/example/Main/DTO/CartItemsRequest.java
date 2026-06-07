package com.example.Main.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Request payload for adding item to cart")
@Data
public class CartItemsRequest {
	
	@Schema(description = "Product ID ",example = "101")
    @NotNull
    private Long productId;

    @NotNull
    @Schema(description = "Quantity of product", example = "2")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}