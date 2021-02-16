package com.example.demo.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
	
	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	
	@Test
	//@Rollback(false)
	public void 회원가입() throws Exception{
		
		//GIVEN
		Member member = new Member();
		member.setName("kim");
		
		
		//WHEN
		Long saveId = memberService.join(member);
		
		
		//THEN
		assertEquals(member, memberRepository.findOne(saveId));
		
	}
	
	@Test(expected = IllegalStateException.class)
	 public void 중복_회원_예외() throws Exception {
	 //Given
		Member member1 = new Member();
		member1.setName("kim");
		
		Member member2 = new Member();
		member2.setName("kim");
	 
	//When
		memberService.join(member1);
		memberService.join(member2); //예외가 발생해야 한다.
	 //Then
		fail("예외가 발생해야 한다.");
	 }
}
