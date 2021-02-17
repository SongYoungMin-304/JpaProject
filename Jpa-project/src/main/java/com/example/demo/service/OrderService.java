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
		
		// �ش� member_id, item_id �� �� member, item ��ȸ
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		// ��� ���� , ȸ���� �ּҸ� �����ͼ� ������� ������ �ϰ�,  ��� �غ�� ���¸� ������Ʈ �Ѵ�.
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setStatus(DeliveryStatus.READY);
		
		//�ֹ���ǰ ����, item, item ����, ������ �־  �ֹ� ��ǰ�� �����Ѵ�.. ITEM���� �����ѰŸ� ���� �۾� ���..
		OrderItem orderItem =  OrderItem.createOrderItem(item, item.getPrice(),count);
		
		//�ֹ� ����  Order��  member�� �ְ� delivery�� �ְ� orderItem�� �޾Ƽ� �ִ´�
		Order order = Order.createOrder(member, delivery, orderItem);
		
		//�ֹ� ����
		orderRepository.save(order);
		return order.getId();
	}
	
	@Transactional
	public void cancelOrder(Long orderId) {
		
		//�ֹ� ��ƼƼ ��ȸ
		Order order = orderRepository.findOne(orderId);
		order.cancel();
	}
	
	
	
}