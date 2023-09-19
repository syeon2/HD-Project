package personal.hdproject.member.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.member.domain.Grade;
import personal.hdproject.member.service.MemberLoginService;
import personal.hdproject.member.service.request.SignInMemberServiceRequest;
import personal.hdproject.member.service.response.JwtTokenResponse;
import personal.hdproject.member.service.response.MemberInfoResponse;
import personal.hdproject.member.service.response.SignInMemberResponse;
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

		given(memberLoginService.login(any(SignInMemberServiceRequest.class)))
			.willReturn(SignInMemberResponse.builder()
				.member(MemberInfoResponse.builder()
					.id(1L)
					.email(email)
					.phone("00011112222")
					.nickname("nickname")
					.address("seoul")
					.grade(Grade.BASIC)
					.build())
				.token(JwtTokenResponse.builder()
					.accessToken("accessToken")
					.refreshToken("refreshToken")
					.build())
				.build()
			);

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/sign-in")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.member.email").value(email))
			.andDo(document("member-login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호")
				),
				responseFields(
					fieldWithPath("data.member.id").type(JsonFieldType.NUMBER)
						.description("회원 아이디"),
					fieldWithPath("data.member.email").type(JsonFieldType.STRING)
						.description("회원 이메일"),
					fieldWithPath("data.member.phone").type(JsonFieldType.STRING)
						.description("회원 전화번호"),
					fieldWithPath("data.member.nickname").type(JsonFieldType.STRING)
						.description("회원 닉네임"),
					fieldWithPath("data.member.address").type(JsonFieldType.STRING)
						.description("회원 주소"),
					fieldWithPath("data.member.grade").type(JsonFieldType.STRING)
						.description("회원 등급"),
					fieldWithPath("data.token.accessToken").type(JsonFieldType.STRING)
						.description("Access Token"),
					fieldWithPath("data.token.refreshToken").type(JsonFieldType.STRING)
						.description("Refresh Token")
				)));
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
			.andExpect(status().isOk())
			.andDo(document("member-logout",
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING)
						.description("로그아웃 성공 메시지")
				)));
	}

	@Test
	@DisplayName("Refresh Token을 받아 갱신하는 API 요청입니다.")
	void renewToken() throws Exception {
		// given
		RefreshTokenRequest request = RefreshTokenRequest.builder()
			.id(1L)
			.refreshToken("refreshToken")
			.build();

		given(memberLoginService.validateAndRenewToken(any(RefreshTokenRequest.class)))
			.willReturn(JwtTokenResponse.builder()
				.accessToken("accessToken")
				.refreshToken("refreshToken")
				.build());

		// when  // then
		mockMvc.perform(
				post("/api/v1/auth/refresh-token")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("member-refresh-token",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER)
						.description("회원 아이디"),
					fieldWithPath("refreshToken").type(JsonFieldType.STRING)
						.description("회원 RefreshToken")
				),
				responseFields(
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
						.description("Access Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
						.optional()
						.description("Refresh Token")
				)));
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
