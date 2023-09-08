package personal.hdproject.customer.web.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.customer.service.request.SignInCustomerServiceRequest;
import personal.hdproject.util.encryption.Sha256Util;

@Getter
public class SignInCustomerRequest {

	@NotBlank(message = "이메일을 입력해주세요.")
	private final String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private final String password;

	@Builder
	private SignInCustomerRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public SignInCustomerServiceRequest toServiceRequest() {
		String encryptedPassword = Sha256Util.getEncrypt(this.password);

		return SignInCustomerServiceRequest.builder()
			.email(this.email)
			.encryptedPassword(encryptedPassword)
			.build();
	}
}
