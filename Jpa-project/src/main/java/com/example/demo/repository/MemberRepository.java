package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository
//@RequiredArgsConstructor // 스프링 데이터 JPA 가 있는 경우에는 @PersistenceContext 생략가능
public class MemberRepository {
	
	@PersistenceContext
	EntityManager em;
	
	public void save(Member member) {
		em.persist(member);
	}
	
	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}
	
	public List<Member> findAll(){
		return em.createQuery("select m from Member m",Member.class)
				.getResultList();
	}
	
	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}
