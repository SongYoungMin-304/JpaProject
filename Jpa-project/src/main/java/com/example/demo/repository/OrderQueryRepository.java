package com.example.demo.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Order;
import com.example.demo.domain.OrderSearch;
import com.example.demo.repository.order.query.OrderFlatDto;
import com.example.demo.repository.order.query.OrderItemQueryDto;
import com.example.demo.repository.order.query.OrderQueryDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders();

		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderItems(orderItems);
		});
		return result;
	}

	public List<OrderQueryDto> findOrders() {
		return em.createQuery(
				"select new com.example.demo.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate,"
						+ "o.status, d.address)" + " from Order o" + " join o.member m" + " join o.delivery d",
				OrderQueryDto.class).getResultList();
	}

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
				"select new com.example.demo.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
						+ " from OrderItem oi" + " join oi.item i" + " where oi.order.id = : orderId",
				OrderItemQueryDto.class).setParameter("orderId", orderId).getResultList();
	}

	//
	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();

		Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));
		
		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
		
		return result;
	}

	private List<Long> toOrderIds(List<OrderQueryDto> result) {
		return result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
	}

	private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
		List<OrderItemQueryDto> orderItems = em.createQuery(
				"select new com.example.demo.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"
						+ " from OrderItem oi" + " join oi.item i" + " where oi.order.id in :orderIds",
				OrderItemQueryDto.class).setParameter("orderIds", orderIds).getResultList();
		return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
	}
	
	
	
	//
	public List<OrderFlatDto> findAllByDto_flat(){
		return em.createQuery(
				"select new com.example.demo.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, "
				+ "o.status, d.address, i.name, oi.orderPrice, oi.count)"
				+" from Order o" 
				+" join o.member m"
				+" join o.delivery d"
				+" join o.orderItems oi"
				+" join oi.item i", OrderFlatDto.class)
				.getResultList();
	}
	
	
	/*
	 * public List<Order> findAll(OrderSearch orderSearch) { QOrder order =
	 * QOrder.order; QMember member = QMember.member; return
	 * query.select(order).from(order).join(order.member, member)
	 * .where(statusEq(orderSearch.getOrderStatus()),
	 * nameLike(orderSearch.getMemberName())).limit(1000) .fetch(); }
	 * 
	 * private BooleanExpression statusEq(OrderStatus statusCond) { if (statusCond
	 * == null) { return null; } return order.status.eq(statusCond); }
	 * 
	 * private BooleanExpression nameLike(String nameCond) { if
	 * (!StringUtils.hasText(nameCond)) { return null; } return
	 * member.name.like(nameCond); }
	 */
}
