package personal.hdproject.member.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.member.domain.Member;

@Getter
public class SignInMemberResponse {

	private final MemberInfoResponse member;
	private final JwtTokenResponse token;

	@Builder
	public SignInMemberResponse(MemberInfoResponse member, JwtTokenResponse token) {
		this.member = member;
		this.token = token;
	}

	public static SignInMemberResponse toResponse(Member member, JwtTokenResponse jwtToken) {
		return SignInMemberResponse.builder()
			.member(MemberInfoResponse.toMemberResponse(member))
			.token(jwtToken)
			.build();
	}
}
