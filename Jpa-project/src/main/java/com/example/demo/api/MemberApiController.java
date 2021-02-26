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

	// 엔티티를 Request Body에 직접 매핑
	// 엔티티에 api 검증을 위한 로직 들어감(@NotEmpty 등등)
	// 실무에서는 회원 엔티티를 위한 api 가 다양하게 만들어지는데, 한 엔티티에 각각의 api를 위한 요청 사항 세팅 어려움
	// 엔티티가 변경 되면 api 스페 ㄱ변함
	// -> api 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
	
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	
	// 별도의 DTO를 파라미터로 세팅
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
		// 엔티티 -> DTO 변환
		
		List<MemberDto> collect = findMembers.stream()
				.map(m -> new MemberDto(m.getName()))
				.collect(Collectors.toList());
		
		return new Result(collect);
	}
	
	// API Entity 정의
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
