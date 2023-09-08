package personal.hdproject.member.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.service.MemberProfileService;
import personal.hdproject.member.web.request.ChangeNicknameRequest;
import personal.hdproject.member.web.request.ChangePhoneRequest;
import personal.hdproject.member.web.request.CreateMemberRequest;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class MemberProfileController {

	private final MemberProfileService memberProfileService;

	@PostMapping("/api/v1/member")
	public ApiResult<Long> joinCustomer(@Valid @RequestBody CreateMemberRequest request) {
		Long joinCustomerId = memberProfileService.join(request.toServiceRequest());

		return ApiResult.onSuccess(joinCustomerId);
	}

	@PostMapping("/api/v1/member-nickname/{id}")
	public ApiResult<Long> changeCustomerNickname(@Valid @RequestBody ChangeNicknameRequest request,
		@PathVariable Long id) {
		Long changedCustomerId = memberProfileService.changeNickname(id, request.getNickname());

		return ApiResult.onSuccess(changedCustomerId);
	}

	@PostMapping("/api/v1/member-phone/{id}")
	public ApiResult<Long> changeCustomerPhone(@Valid @RequestBody ChangePhoneRequest request, @PathVariable Long id) {
		Long changedCustomerId = memberProfileService.changePhone(id, request.getPhone());

		return ApiResult.onSuccess(changedCustomerId);
	}

	@DeleteMapping("/api/v1/member/{id}")
	public ApiResult<String> deleteCustomerAccount(@PathVariable Long id) {
		memberProfileService.deleteAccount(id);

		return ApiResult.onSuccess("계정이 삭제되었습니다.");
	}
}
