package personal.hdproject.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import personal.hdproject.member.service.MemberLoginService;
import personal.hdproject.member.service.response.JwtTokenResponse;
import personal.hdproject.member.service.response.SignInMemberResponse;
import personal.hdproject.member.web.request.RefreshTokenRequest;
import personal.hdproject.member.web.request.SignInMemberRequest;
import personal.hdproject.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class MemberLoginController {

	@Value("${jwt.header}")
	private String header;

	private final MemberLoginService memberLoginService;

	@PostMapping("/api/v1/member/sign-in")
	public ApiResult<SignInMemberResponse> signInMember(@Valid @RequestBody SignInMemberRequest request) {
		SignInMemberResponse response = memberLoginService.login(request.toServiceRequest());

		return ApiResult.onSuccess(response);
	}

	@PostMapping("/api/v1/member/sign-out")
	public ApiResult<String> signOutMember(HttpServletRequest request) {
		String accessToken = request.getHeader(header);

		memberLoginService.logout(accessToken);

		return ApiResult.onSuccess("로그아웃 되었습니다.");
	}

	@PostMapping("/api/v1/member/refresh-token")
	public ApiResult<JwtTokenResponse> getJwtToken(@Valid @RequestBody RefreshTokenRequest request) {
		JwtTokenResponse response = memberLoginService.validateAndRenewToken(request);

		return ApiResult.onSuccess(response);
	}
}
