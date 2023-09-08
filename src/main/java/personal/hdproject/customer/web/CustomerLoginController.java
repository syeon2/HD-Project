package personal.hdproject.customer.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.customer.service.CustomerLoginService;
import personal.hdproject.customer.service.response.JWTTokenResponse;
import personal.hdproject.customer.service.response.SignInCustomerResponse;
import personal.hdproject.customer.web.request.RefreshTokenRequest;
import personal.hdproject.customer.web.request.SignInCustomerRequest;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class CustomerLoginController {

	@Value("${jwt.header}")
	private String header;

	private final CustomerLoginService customerLoginService;

	@PostMapping("/api/v1/customer/sign-in")
	public ApiResult<SignInCustomerResponse> signInCustomer(@Valid @RequestBody SignInCustomerRequest request) {
		SignInCustomerResponse response = customerLoginService.login(request.toServiceRequest());

		return ApiResult.onSuccess(response);
	}

	@PostMapping("/api/v1/customer/sign-out")
	public ApiResult<String> signOutCustomer(HttpServletRequest request) {
		String accessToken = request.getHeader(header);

		customerLoginService.logout(accessToken);

		return ApiResult.onSuccess("로그아웃 되었습니다.");
	}

	@PostMapping("/api/v1/customer/refresh-token")
	public ApiResult<JWTTokenResponse> getJWTToken(@Valid @RequestBody RefreshTokenRequest request) {
		JWTTokenResponse response = customerLoginService.validateAndRenewToken(request);

		return ApiResult.onSuccess(response);
	}
}
