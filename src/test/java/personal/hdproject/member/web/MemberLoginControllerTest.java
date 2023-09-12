package personal.hdproject.member.web;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.member.service.MemberLoginService;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.member.web.request.RefreshTokenRequest;
import personal.hdproject.member.web.request.SignInMemberRequest;

@WebMvcTest(controllers = MemberLoginController.class)
class MemberLoginControllerTest extends BaseTestConfig {

	@MockBean
	private MemberLoginService memberLoginService;

	@Test
	@DisplayName("로그인 API를 호출합니다.")
	void signInMember() throws Exception {
		// given
		String email = "1234@gmail.com";
		SignInMemberRequest request = SignInMemberRequest.builder()
			.email(email)
			.password("12345678")
			.build();

		SignInMemberServiceRequest serviceRequest = request.toServiceRequest();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/sign-in")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그인 API를 호출합니다. - 이메일이 없으면 에러를 반환합니다.")
	void signInMember_NonEmail() throws Exception {
		// given
		SignInMemberRequest request = SignInMemberRequest.builder()
			.email("")
			.password("12345678")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/sign-in")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("이메일을 입력해주세요."));
	}

	@Test
	@DisplayName("로그인 API를 호출합니다. - 비밀번호가 없으면 에러를 반환합니다.")
	void signInMember_NonPassword() throws Exception {
		// given
		SignInMemberRequest request = SignInMemberRequest.builder()
			.email("1234@gmail.com")
			.password("")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/sign-in")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("비밀번호를 입력해주세요."));
	}

	@Test
	@DisplayName("로그아웃 API를 호출합니다.")
	void logout() throws Exception {
		// given  // when  // then
		mockMvc.perform(
				post("/api/v1/auth/sign-out")
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Refresh Token을 받아 갱신하는 API 요청입니다.")
	void renewToken() throws Exception {
		// given
		RefreshTokenRequest request = RefreshTokenRequest.builder()
			.id(1L)
			.refreshToken("refreshToken")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Refresh Token을 받아 갱신하는 API 요청입니다. - Id가 누락되면 에러를 반환합니다.")
	void renewToken_NonId() throws Exception {
		// given
		RefreshTokenRequest request = RefreshTokenRequest.builder()
			.refreshToken("refreshToken")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("아이디는 필수 값입니다."));
	}

	@Test
	@DisplayName("Refresh Token을 받아 갱신하는 API 요청입니다. - RefreshToken이 누락되면 에러를 반환합니다.")
	void renewToken_NonRefreshToken() throws Exception {
		// given
		RefreshTokenRequest request = RefreshTokenRequest.builder()
			.id(1L)
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("Refresh Token은 필수 값입니다."));
	}
}
