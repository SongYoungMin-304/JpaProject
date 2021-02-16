package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // ��Ͽ��� �����ϴ� ������
public class MemberService {
	
	/*
	 * @Autowired MemberRepository memberRepository;
	 */
	
	private final MemberRepository memberRepository;
	
	@Transactional
	public Long join(Member member) {
		
		validateDuplicateMember(member);
		
		memberRepository.save(member);
		return member.getId();
	}
	
	private void validateDuplicateMember(Member member) {
		List<Member> findMembers =
				memberRepository.findByName(member.getName());
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("�̹� �����ϴ� ȸ���Դϴ�.");
		}
	}
	
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

}