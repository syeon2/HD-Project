package personal.hdproject.member.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.member.service.MemberProfileService;
import personal.hdproject.member.service.request.CreateMemberServiceRequest;
import personal.hdproject.member.web.request.ChangeNicknameRequest;
import personal.hdproject.member.web.request.ChangePhoneRequest;
import personal.hdproject.member.web.request.CreateMemberRequest;

@WebMvcTest(controllers = MemberProfileController.class)
class MemberProfileControllerTest extends BaseTestConfig {

	@MockBean
	private MemberProfileService memberProfileService;

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다.")
	void createMember() throws Exception {
		// given
		long memberId = 1L;
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		given(memberProfileService.join(any(CreateMemberServiceRequest.class)))
			.willReturn(memberId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(memberId))
			.andDo(document("member-join",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("회원가입한 회원 아이디")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 이메일 형식이 다르면 예외를 반환합니다.")
	void createMember_unsupportedEmailFormat() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("이메일 형식으로 작성해주세요.(ex. xxxx@xxxx.com"))
			.andDo(document("member-join-exception-email-pattern",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 이메일이 없으면 예외를 반환합니다.")
	void createMember_withoutEmail() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty())
			.andDo(document("member-join-exception-email-empty",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 비밀번호가 8자 미만이면 예외를 반환홥니다.")
	void createMember_minPasswordLength() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("1234567")
			.nickname("nickname")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("비밀번호는 최소 8자 이상 작성해주세요"))
			.andDo(document("member-join-exception-password-min",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 비밀번호가 없으면 예외를 반환합니다.")
	void createMember_withoutPassword() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("")
			.nickname("nickname")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty())
			.andDo(document("member-join-exception-password-empty",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 닉네임이 공백이면 예외를 반환합니다.")
	void createMember_notBlankNickname() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("")
			.phone("00011112222")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("닉네임은 공백을 허용하지 않습니다."))
			.andDo(document("member-join-exception-nickname-empty",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 전화번호가 없으면 예외를 반환합니다.")
	void createMember_withoutPhone() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty())
			.andDo(document("member-join-exception-phone-empty",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 문자가 10 ~ 11자리 미만이면 예외를 반환합니다.")
	void createMember_unsupportedPhoneFormat_min() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andDo(document("member-join-exception-phone-pattern",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 문자가 10 ~ 11자리 초과면 예외를 반환합니다.")
	void createMember_unsupportedPhoneFormat_max() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789012")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andDo(document("member-join-exception-phone-over",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 전화번호가 문자면 예외를 반환합니다.")
	void createMember_unsupportedPhoneFormat_notNumber() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789a")
			.address("서울 강남구")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."))
			.andDo(document("member-join-exception-phone-string",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 주소는 필수 값입니다.")
	void createMember_nnAddress() throws Exception {
		// given
		CreateMemberRequest request = CreateMemberRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("1234567891")
			.address("")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("주소는 필수 값입니다."))
			.andDo(document("member-join-exception-address-require",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING)
						.description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING)
						.description("비밀번호"),
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임"),
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호"),
					fieldWithPath("address").type(JsonFieldType.STRING)
						.description("주소")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("exception")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 닉네임을 수정합니다.")
	void changeNickname() throws Exception {
		// given
		Long memberId = 1L;
		ChangeNicknameRequest request = new ChangeNicknameRequest("nickname");

		given(memberProfileService.changeNickname(anyLong(), anyString()))
			.willReturn(memberId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member-nickname/" + memberId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(memberId))
			.andDo(document("member-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("수정된 회원 아이디")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 닉네임을 수정합니다. - 닉네임은 공백을 허용하지 않습니다.")
	void changeNickname_unsupportedBlank() throws Exception {
		// given
		Long memberId = 1L;
		ChangeNicknameRequest request = new ChangeNicknameRequest("");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member-nickname/" + memberId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("닉네임은 공백을 허용하지 않습니다."))
			.andDo(document("member-update-nickname",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING)
						.description("닉네임")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("닉네임 예외")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 전화번호를 수정합니다.")
	void changePhone() throws Exception {
		// given
		Long memberId = 1L;
		ChangePhoneRequest request = new ChangePhoneRequest("00011112222");

		given(memberProfileService.changePhone(anyLong(), anyString())).willReturn(memberId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member-phone/" + memberId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(memberId))
			.andDo(document("member-update-phone",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.NUMBER)
						.description("수정된 회원 아이디")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 전화번호를 수정합니다. - 10 ~ 11자리의 숫자만 입력 가능합니다.")
	void changePhone_unsupportedFormat() throws Exception {
		// given
		Long memberId = 1L;
		ChangePhoneRequest request = new ChangePhoneRequest("");

		// when  // then
		mockMvc.perform(
				post("/api/v1/member-phone/" + memberId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty())
			.andDo(document("member-update-phone-exception-empty",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("phone").type(JsonFieldType.STRING)
						.description("전화번호")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING)
						.description("수정 예외 메시지")
				)));
	}

	@Test
	@DisplayName(value = "API를 호출하여 계정을 삭제합니다.")
	void deleteAccount() throws Exception {
		// given
		Long memberId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/member/{member_id}", memberId)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value("계정이 삭제되었습니다."))
			.andExpect(jsonPath("$.message").doesNotExist())
			.andDo(document("member-delete",
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("member_id").description("회원 아이디")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.STRING)
						.description("회원 삭제 메시지")
				)));
	}
}
