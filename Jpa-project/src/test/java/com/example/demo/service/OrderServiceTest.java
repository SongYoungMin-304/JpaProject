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
	public void 상품주문() throws Exception{
		
		//Given
		Member member = createMember();
		Item item = createBook("시골 JPA", 10000, 10);
		int orderCount = 2;
		
		//When
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		//Then
		Order getOrder = orderRepository.findOne(orderId);
		
		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격  * 수량이다.", 10000 * 2, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());
	}
	
	@Test(expected = NotEnoughStockException.class)
	 public void 상품주문_재고수량초과() throws Exception {
		
		//Given
		// 멤버를 생성하고  ITEM BOOK를 생성한다.  재고보다 많은 숫자 세팅
		Member member = createMember();
		Item item = createBook("책", 10000, 10);
	
		int orderCount = 11;
		
		//When
		// 주문을 만든다.
		orderService.order(member.getId(), item.getId(), orderCount);
		
		//Then  .fail 을 통해서 예외발생을 보여준다.
		fail("재고 수량 부족 예외가 발생해야 한다.");
	 }
	
	@Test
	public void 주문취소() {
		//Given
		//When
		//Then
		
		
		//Given 멤버를 생성하고   Book 를 생성하고 주문을 한다.
		Member member = createMember();
		Item item = createBook("책책", 10000, 12);
		
		Long orderId = orderService.order(member.getId(), item.getId(), 2);
		
		//When 주문을 취소한다.
		orderService.cancelOrder(orderId);
		
		//Then 1. 상태가 취소가 맞는지 확인       2. 갯수 확인
		Order order = orderRepository.findOne(orderId);		
		assertEquals("주문 취소시 상태는 Cancel 이다.",OrderStatus.CANCEL,  order.getStatus());
		
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 12, item.getStockQuantity());
		
	}
	
	public Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
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
