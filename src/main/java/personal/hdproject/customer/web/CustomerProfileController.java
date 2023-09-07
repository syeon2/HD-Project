package personal.hdproject.customer.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.customer.service.CustomerProfileService;
import personal.hdproject.customer.web.request.ChangeNicknameRequest;
import personal.hdproject.customer.web.request.ChangePhoneRequest;
import personal.hdproject.customer.web.request.CreateCustomerRequest;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class CustomerProfileController {

	private final CustomerProfileService customerProfileService;

	@PostMapping("/api/v1/customer")
	public ApiResult<Long> joinCustomer(@Valid @RequestBody CreateCustomerRequest request) {
		Long joinCustomerId = customerProfileService.join(request.toServiceRequest());

		return ApiResult.onSuccess(joinCustomerId);
	}

	@PostMapping("/api/v1/customer-nickname/{id}")
	public ApiResult<Long> changeCustomerNickname(@Valid @RequestBody ChangeNicknameRequest request,
		@PathVariable Long id) {
		Long changedCustomerId = customerProfileService.changeNickname(id, request.getNickname());

		return ApiResult.onSuccess(changedCustomerId);
	}

	@PostMapping("/api/v1/customer-phone/{id}")
	public ApiResult<Long> changeCustomerPhone(@Valid @RequestBody ChangePhoneRequest request, @PathVariable Long id) {
		Long changedCustomerId = customerProfileService.changePhone(id, request.getPhone());

		return ApiResult.onSuccess(changedCustomerId);
	}

	@DeleteMapping("/api/v1/customer/{id}")
	public ApiResult<String> deleteCustomerAccount(@PathVariable Long id) {
		customerProfileService.deleteAccount(id);

		return ApiResult.onSuccess("계정이 삭제되었습니다.");
	}
}
