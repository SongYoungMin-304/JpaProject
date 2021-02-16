package com.example.demo.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.example.demo.domain.Item;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item{
	
	private String author;
	private String isbn;
}
