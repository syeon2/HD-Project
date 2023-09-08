package personal.hdproject.member.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.member.dao.MemberRepository;
import personal.hdproject.member.dao.RefreshTokenRepository;
import personal.hdproject.member.domain.Grade;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.member.service.response.SignInMemberResponse;
import personal.hdproject.member.web.request.SignInMemberRequest;

@SpringBootTest
class MemberLoginServiceTest {

	@Autowired
	private MemberLoginService memberLoginService;

	@Autowired
	private MemberProfileService memberProfileService;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@BeforeEach
	void after() {
		memberRepository.deleteAll();
	}

	@Test
	@DisplayName("회원이 로그인을 합니다.")
	void login() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "ello";

		String phone = "0001111232";
		CreateMemberServiceRequest request = CreateMemberServiceRequest.builder()
			.email(email)
			.password(password)
			.phone(phone)
			.nickname(nickname)
			.build();

		memberProfileService.join(request);

		// when
		SignInMemberServiceRequest serviceRequest = SignInMemberRequest.builder()
			.email(email)
			.password(password)
			.build().toServiceRequest();

		SignInMemberResponse loginMember = memberLoginService.login(serviceRequest);

		// then
		assertThat(loginMember.getToken().getAccessToken()).isNotBlank();
		assertThat(loginMember.getToken().getRefreshToken()).isNotBlank();

		assertThat(loginMember.getMember())
			.extracting("email", "nickname", "phone", "grade")
			.contains(email, nickname, phone, Grade.BASIC);

		assertThat(refreshTokenRepository.getRefreshToken(loginMember.getMember().getId())).isNotNull();
	}

	@Test
	@DisplayName("회원이 로그아웃을 합니다.")
	void logout() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "ello";

		String phone = "0001111232";
		CreateMemberServiceRequest request = CreateMemberServiceRequest.builder()
			.email(email)
			.password(password)
			.phone(phone)
			.nickname(nickname)
			.build();

		memberProfileService.join(request);

		SignInMemberServiceRequest serviceRequest = SignInMemberRequest.builder()
			.email(email)
			.password(password)
			.build().toServiceRequest();

		SignInMemberResponse loginMember = memberLoginService.login(serviceRequest);

		// when
		memberLoginService.logout(loginMember.getToken().getAccessToken());

		// then
		assertThat(refreshTokenRepository.getRefreshToken(loginMember.getMember().getId())).isNull();
	}
}
