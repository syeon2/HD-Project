package personal.hdproject.customer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.customer.dao.CustomerRepository;
import personal.hdproject.customer.domain.Customer;
import personal.hdproject.customer.service.request.SignInCustomerServiceRequest;
import personal.hdproject.customer.service.response.JWTTokenResponse;
import personal.hdproject.customer.service.response.SignInCustomerResponse;
import personal.hdproject.customer.web.request.RefreshTokenRequest;
import personal.hdproject.util.error.exception.LoginException;
import personal.hdproject.util.jwt.JwtAuthTokenProvider;

@Service
@RequiredArgsConstructor
public class CustomerLoginService {

	private final CustomerRepository customerRepository;
	private final JwtAuthTokenProvider jwtAuthTokenProvider;

	public SignInCustomerResponse login(SignInCustomerServiceRequest request) {
		Customer customer = checkAccountAndGetCustomer(request.getEmail(), request.getEncryptedPassword());
		JWTTokenResponse jwtToken = jwtAuthTokenProvider.generateToken(customer.getId());

		return SignInCustomerResponse.toResponse(customer, jwtToken);
	}

	public void logout(String accessToken) {
		jwtAuthTokenProvider.removeRefreshTokenInStorage(accessToken);
	}

	public JWTTokenResponse validateAndRenewToken(RefreshTokenRequest request) {
		return jwtAuthTokenProvider.generateRenewToken(request.getId(), request.getRefreshToken());
	}

	private Customer checkAccountAndGetCustomer(String email, String encryptedPassword) {
		Optional<Customer> findCustomerByEmailOptional = customerRepository.findCustomerByEmail(email);

		Customer customer = checkEmailAndGetCustomer(findCustomerByEmailOptional);
		checkPassword(customer, encryptedPassword);

		return customer;
	}

	private Customer checkEmailAndGetCustomer(Optional<Customer> findCustomerByEmailOptional) {
		return findCustomerByEmailOptional
			.orElseThrow(() -> new LoginException("아이디가 존재하지 않습니다."));
	}

	private void checkPassword(Customer customer, String encryptedPassword) {
		if (!customer.getPassword().equals(encryptedPassword)) {
			throw new LoginException("비밀번호가 일치하지 않습니다.");
		}
	}
}
