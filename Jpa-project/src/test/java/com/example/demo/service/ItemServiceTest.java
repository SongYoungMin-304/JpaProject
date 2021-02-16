package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Category;
import com.example.demo.domain.Item;
import com.example.demo.domain.item.Book;
import com.example.demo.repository.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
	
	@Autowired
	ItemRepository itemRepository;
	
	@Test
	//@Rollback(false)
	public void 아이템저장() throws Exception
	{
		Item item = new Book();
		item.setName("송영민");
		item.setPrice(1000);
		item.setStockQuantity(1);
		
		List<Category> categories = new ArrayList<Category>();
		Category category = new Category();
		category.setName("책");
		categories.add(category);
		
		item.setCategories( categories);
		
		itemRepository.save(item);
		
		assertEquals(itemRepository.findOne(item.getId()), item);
	}
	
	@Test
	//@Rollback(false)
	public void 아이템재고추가() throws Exception
	{
		Item item = new Book();
		item.setName("송영민");
		item.setPrice(1000);
		item.setStockQuantity(1);
		
		List<Category> categories = new ArrayList<Category>();
		Category category = new Category();
		category.setName("책");
		categories.add(category);
		
		item.setCategories( categories);
		
		itemRepository.save(item);
		
		
		int orgin = item.getStockQuantity();
		
		int addNum = 100;
		item.addStock(addNum);
		
		assertEquals(item.getStockQuantity(), orgin + addNum);
	}

}
