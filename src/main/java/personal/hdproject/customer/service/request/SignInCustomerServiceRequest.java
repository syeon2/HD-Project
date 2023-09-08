package personal.hdproject.customer.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInCustomerServiceRequest {

	private final String email;
	private final String encryptedPassword;

	@Builder
	private SignInCustomerServiceRequest(String email, String encryptedPassword) {
		this.email = email;
		this.encryptedPassword = encryptedPassword;
	}
}
