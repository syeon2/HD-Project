package personal.hdproject.member.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInMemberServiceRequest {

	private final String email;
	private final String encryptedPassword;

	@Builder
	private SignInMemberServiceRequest(String email, String encryptedPassword) {
		this.email = email;
		this.encryptedPassword = encryptedPassword;
	}
}
