package com.example.demo.repository.order.query;

import java.time.LocalDateTime;

import com.example.demo.domain.Address;
import com.example.demo.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderFlatDto {

	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private Address address;
	private OrderStatus orderStatus;

	private String itemName;
	private int orderPrice;
	private int count;

	public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address adress,
			String itemName, int orderPrice, int count) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}

}
