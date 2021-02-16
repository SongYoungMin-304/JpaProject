package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

import com.example.demo.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
	
	@Id @GeneratedValue
	@Column(name = "item_id")
	private Long id;
	
	private String name;
	
	private int price;
	
	private int stockQuantity;
	
	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<Category>();
	
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	public void removeStock(int quantity) {
		int resStock = this.stockQuantity - quantity;
		if(resStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		
		this.stockQuantity = resStock;
	}
	
}