package personal.hdproject.member.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.member.domain.Grade;
import personal.hdproject.member.domain.Member;

@Getter
public class MemberInfoResponse {

	private final Long id;
	private final String email;
	private final String nickname;
	private final String phone;
	private final Grade grade;

	@Builder
	private MemberInfoResponse(Long id, String email, String nickname, String phone, Grade grade) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.phone = phone;
		this.grade = grade;
	}

	public static MemberInfoResponse toMemberResponse(Member member) {
		return MemberInfoResponse.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.phone(member.getPhone())
			.grade(member.getGrade())
			.build();
	}
}
