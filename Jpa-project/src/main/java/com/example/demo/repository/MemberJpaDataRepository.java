package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Member;

public interface MemberJpaDataRepository extends JpaRepository<Member, Long>{
	
	List<Member> findByName(String name);
}
