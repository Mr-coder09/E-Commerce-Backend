package com.example.Main.Enum;

import com.example.Main.Exceptions.BadRequestException;

public enum OrderStatus {

	CREATED, PAID, CANCELLED, SHIPPED, DELIVERED, REFUNDED;

	public static OrderStatus fromString(String status) {

		if (status == null || status.isBlank()) {
			throw new BadRequestException("Order status cannot be null or empty");
		}

		for (OrderStatus orderStatus : OrderStatus.values()) {

			if (orderStatus.name().equalsIgnoreCase(status)) {
				return orderStatus;
			}

		}

		throw new BadRequestException("Invalid order status: " + status);

	}

}
