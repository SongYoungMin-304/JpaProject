package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Delivery;
import com.example.demo.domain.DeliveryStatus;
import com.example.demo.domain.Item;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		
		// 해당 member_id, item_id 로 각 member, item 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		// 배송 생성 , 회원의 주소를 가져와서 배송지에 세팅을 하고,  배송 준비로 상태를 업데이트 한다.
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		
		//주문상품 생성, item, item 가격, 갯수를 넣어서  주문 상품을 세팅한다.. ITEM에서 구매한거를 뺴는 작업 등등..
		OrderItem orderItem =  OrderItem.createOrderItem(item, item.getPrice(),count);
		
		//주문 생성  Order에  member을 넣고 delivery를 넣고 orderItem을 받아서 넣는다
		Order order = Order.createOrder(member, delivery, orderItem);
		
		//주문 저장
		orderRepository.save(order);
		return order.getId();
	}
	
	@Transactional
	public void cancelOrder(Long orderId) {
		
		//주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		order.cancel();
	}
	
	
	
}
