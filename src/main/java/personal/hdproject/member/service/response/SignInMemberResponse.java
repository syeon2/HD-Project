package personal.hdproject.member.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.member.domain.Member;

@Getter
public class SignInMemberResponse {

	private final MemberInfoResponse member;
	private final JWTTokenResponse token;

	@Builder
	public SignInMemberResponse(MemberInfoResponse member, JWTTokenResponse token) {
		this.member = member;
		this.token = token;
	}

	public static SignInMemberResponse toResponse(Member member, JWTTokenResponse jwtToken) {
		return SignInMemberResponse.builder()
			.member(MemberInfoResponse.toMemberResponse(member))
			.token(jwtToken)
			.build();
	}
}
