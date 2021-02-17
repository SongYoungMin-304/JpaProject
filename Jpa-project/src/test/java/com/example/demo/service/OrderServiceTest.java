package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Address;
import com.example.demo.domain.Item;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.domain.item.Book;
import com.example.demo.exception.NotEnoughStockException;
import com.example.demo.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
	
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Test
	public void ��ǰ�ֹ�() throws Exception{
		
		//Given
		Member member = createMember();
		Item item = createBook("�ð� JPA", 10000, 10);
		int orderCount = 2;
		
		//When
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		//Then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals("��ǰ �ֹ��� ���´� ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("�ֹ��� ��ǰ ���� ���� ��Ȯ�ؾ� �Ѵ�", 1, getOrder.getOrderItems().size());
		assertEquals("�ֹ� ������ ����  * �����̴�.", 10000 * 2, getOrder.getTotalPrice());
		assertEquals("�ֹ� ������ŭ ����� �پ�� �Ѵ�.", 8, item.getStockQuantity());
	}
	
	@Test(expected = NotEnoughStockException.class)
	 public void ��ǰ�ֹ�_��������ʰ�() throws Exception {
		
		//Given
		// ����� �����ϰ�  ITEM BOOK�� �����Ѵ�.  ������� ���� ���� ����
		Member member = createMember();
		Item item = createBook("å", 10000, 10);
	
		int orderCount = 11;
		
		//When
		// �ֹ��� �����.
		orderService.order(member.getId(), item.getId(), orderCount);
		
		//Then  .fail �� ���ؼ� ���ܹ߻��� �����ش�.
		fail("��� ���� ���� ���ܰ� �߻��ؾ� �Ѵ�.");
	 }
	
	@Test
	public void �ֹ����() {
		//Given
		//When
		//Then
		
		
		//Given ����� �����ϰ�   Book �� �����ϰ� �ֹ��� �Ѵ�.
		Member member = createMember();
		Item item = createBook("åå", 10000, 12);
		
		Long orderId = orderService.order(member.getId(), item.getId(), 2);
		
		//When �ֹ��� ����Ѵ�.
		orderService.cancelOrder(orderId);
		
		//Then 1. ���°� ��Ұ� �´��� Ȯ��       2. ���� Ȯ��
		Order order = orderRepository.findOne(orderId);		
		assertEquals("�ֹ� ��ҽ� ���´� Cancel �̴�.",OrderStatus.CANCEL,  order.getStatus());
		
		assertEquals("�ֹ��� ��ҵ� ��ǰ�� �׸�ŭ ����� �����ؾ� �Ѵ�.", 12, item.getStockQuantity());
		
	}
	
	public Member createMember() {
		Member member = new Member();
		member.setName("ȸ��1");
		member.setAddress(new Address("����", "����", "123-123"));
		em.persist(member);
		return member;
	}
	
	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setStockQuantity(stockQuantity);
		book.setPrice(price);
		em.persist(book);
		return book;
	}

}