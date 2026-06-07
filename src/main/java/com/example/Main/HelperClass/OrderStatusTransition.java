package com.example.Main.HelperClass;

import com.example.Main.Enum.OrderStatus;
import com.example.Main.Exceptions.BadRequestException;

public class OrderStatusTransition {

	public static void validateTransition(OrderStatus currentStatus, OrderStatus requestedStatus) {

		if (currentStatus == requestedStatus) {
			throw new BadRequestException(" It is already  " +currentStatus);
		}

		switch (currentStatus) {
		
		case CREATED: {

			if (requestedStatus == OrderStatus.PAID || requestedStatus == OrderStatus.CANCELLED) {

				return ;

			}
			throw new BadRequestException("CREATED order can move only to PAID or CANCELLED");
		}
		case PAID: {

			if (requestedStatus == OrderStatus.SHIPPED || requestedStatus == OrderStatus.CANCELLED) {
				return;
			}
			throw new BadRequestException("Paid order can move only to SHIPPED or CANCELLED");

		}

		case SHIPPED: {
			if (requestedStatus == OrderStatus.DELIVERED) {
				return;
			}
			throw new BadRequestException("SHIPPED order can move only to DELIVERED ");
		}

		case DELIVERED: {
			if (requestedStatus == OrderStatus.REFUNDED) {
				return;
			}
			throw new BadRequestException("DELIVERED order can move only to REFUNDED");
		}
		default:
			
			 throw new BadRequestException("Invalid order status: " + currentStatus);
			
		}

	}

}
