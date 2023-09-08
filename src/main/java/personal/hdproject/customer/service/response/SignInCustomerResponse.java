package personal.hdproject.customer.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.customer.domain.Customer;

@Getter
public class SignInCustomerResponse {

	private final CustomerInfoResponse customer;
	private final JWTTokenResponse token;

	@Builder
	public SignInCustomerResponse(CustomerInfoResponse customer, JWTTokenResponse token) {
		this.customer = customer;
		this.token = token;
	}

	public static SignInCustomerResponse toResponse(Customer customer, JWTTokenResponse jwtToken) {
		return SignInCustomerResponse.builder()
			.customer(CustomerInfoResponse.toCustomerResponse(customer))
			.token(jwtToken)
			.build();
	}
}
