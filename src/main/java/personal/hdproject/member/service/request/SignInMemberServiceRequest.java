package personal.hdproject.member.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInMemberServiceRequest {

	private final String email;
	private final String password;

	@Builder
	private SignInMemberServiceRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
