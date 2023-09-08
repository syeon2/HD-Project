package personal.hdproject.member.web.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.util.encryption.Sha256Util;

@Getter
public class SignInMemberRequest {

	@NotBlank(message = "이메일을 입력해주세요.")
	private final String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private final String password;

	@Builder
	private SignInMemberRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public SignInMemberServiceRequest toServiceRequest() {
		String encryptedPassword = Sha256Util.getEncrypt(this.password);

		return SignInMemberServiceRequest.builder()
			.email(this.email)
			.encryptedPassword(encryptedPassword)
			.build();
	}
}
