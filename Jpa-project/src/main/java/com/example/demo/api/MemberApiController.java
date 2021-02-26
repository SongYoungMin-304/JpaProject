package com.example.demo.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	// ��ƼƼ�� Request Body�� ���� ����
	// ��ƼƼ�� api ������ ���� ���� ��(@NotEmpty ���)
	// �ǹ������� ȸ�� ��ƼƼ�� ���� api �� �پ��ϰ� ��������µ�, �� ��ƼƼ�� ������ api�� ���� ��û ���� ���� �����
	// ��ƼƼ�� ���� �Ǹ� api ���� ������
	// -> api ��û ���忡 ���߾� ������ DTO�� �Ķ���ͷ� �޴´�.
	
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	
	// ������ DTO�� �Ķ���ͷ� ����
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request)
	{
		Member member = new Member();
		 member.setName(request.getName());
		 Long id = memberService.join(member);
		 return new CreateMemberResponse(id);
	}
	
	
	@PutMapping("/api/v2/memers/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request)
	{
		memberService.update(id, request.getName());
		Member findmember = memberService.findOne(id);
		return new UpdateMemberResponse(findmember.getId(), findmember.getName());
	}
	
	@GetMapping("/api/v1/members")
	public List<Member> memberV1(){
		return memberService.findMembers();
	}
	
	@GetMapping("/api/v2/memers")
	public Result membersV2() {
		
		List<Member> findMembers = memberService.findMembers();
		// ��ƼƼ -> DTO ��ȯ
		
		List<MemberDto> collect = findMembers.stream()
				.map(m -> new MemberDto(m.getName()))
				.collect(Collectors.toList());
		
		return new Result(collect);
	}
	
	// API Entity ����
	@Data
	static class CreateMemberRequest {
		private String name;
	}

	@Data
	@AllArgsConstructor
	class CreateMemberResponse {
		private Long id;
	}
	
	@Data
	static class UpdateMemberRequest{
		private String name;
	}
	
	@Data
	@AllArgsConstructor
	class UpdateMemberResponse{
		private Long id;
		private String name;
	}
	
	@Data
	@AllArgsConstructor
	class Result<T> {
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	class MemberDto {
		private String name;
	}

}