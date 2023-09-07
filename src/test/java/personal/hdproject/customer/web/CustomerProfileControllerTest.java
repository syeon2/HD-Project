package personal.hdproject.customer.web;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import personal.hdproject.BaseTestConfig;
import personal.hdproject.customer.service.CustomerProfileService;
import personal.hdproject.customer.web.request.ChangeNicknameRequest;
import personal.hdproject.customer.web.request.ChangePhoneRequest;
import personal.hdproject.customer.web.request.CreateCustomerRequest;

@WebMvcTest(controllers = CustomerProfileController.class)
class CustomerProfileControllerTest extends BaseTestConfig {

	@MockBean
	private CustomerProfileService customerProfileService;

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다.")
	void createCustomer() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 이메일 형식이 다르면 예외를 반환합니다.")
	void createCustomer_unsupportedEmailFormat() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("이메일 형식으로 작성해주세요.(ex. xxxx@xxxx.com"));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 이메일이 없으면 예외를 반환합니다.")
	void createCustomer_withoutEmail() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("")
			.password("12345678")
			.nickname("nickname")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 비밀번호가 8자 미만이면 예외를 반환홥니다.")
	void createCustomer_minPasswordLength() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("1234567")
			.nickname("nickname")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("비밀번호는 최소 8자 이상 작성해주세요"));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 비밀번호가 없으면 예외를 반환합니다.")
	void createCustomer_withoutPassword() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("")
			.nickname("nickname")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 닉네임이 공백이면 예외를 반환합니다.")
	void createCustomer_notBlankNickname() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("")
			.phone("00011112222")
			.build();

		// when  // then
		mockMvc.perform(
				post("/api/v1/customer")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("닉네임은 공백을 허용하지 않습니다."));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 전화번호가 없으면 예외를 반환합니다.")
	void createCustomer_withoutPhone() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 문자가 10 ~ 11자리 미만이면 예외를 반환합니다.")
	void createCustomer_unsupportedPhoneFormat_min() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 문자가 10 ~ 11자리 초과면 예외를 반환합니다.")
	void createCustomer_unsupportedPhoneFormat_max() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789012")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."));
	}

	@Test
	@DisplayName(value = "API를 호출하여 새로운 계정을 생성합니다. - 전화번호가 문자면 예외를 반환합니다.")
	void createCustomer_unsupportedPhoneFormat_notNumber() throws Exception {
		// given
		CreateCustomerRequest request = CreateCustomerRequest.builder()
			.email("1234@gmail.com")
			.password("12345678")
			.nickname("nickname")
			.phone("123456789a")
			.build();

		// when  // then
		mockMvc.perform(
			post("/api/v1/customer")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("10 ~ 11자리의 숫자만 입력 가능합니다."));
	}

	@Test
	@DisplayName(value = "API를 호출하여 닉네임을 수정합니다.")
	void changeNickname() throws Exception {
		// given
		Long customerId = 1L;
		ChangeNicknameRequest request = new ChangeNicknameRequest("nickname");

		when(customerProfileService.changeNickname(customerId, "change_nickname")).thenReturn(customerId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/customer-nickname/" + customerId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "API를 호출하여 닉네임을 수정합니다. - 닉네임은 공백을 허용하지 않습니다.")
	void changeNickname_unsupportedBlank() throws Exception {
		// given
		Long customerId = 1L;
		ChangeNicknameRequest request = new ChangeNicknameRequest("");

		// when  // then
		mockMvc.perform(
				post("/api/v1/customer-nickname/" + customerId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").value("닉네임은 공백을 허용하지 않습니다."));
	}

	@Test
	@DisplayName(value = "API를 호출하여 전화번호를 수정합니다.")
	void changePhone() throws Exception {
		// given
		Long customerId = 1L;
		ChangePhoneRequest request = new ChangePhoneRequest("00011112222");

		when(customerProfileService.changePhone(customerId, "00011112222")).thenReturn(customerId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/customer-phone/" + customerId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "API를 호출하여 전화번호를 수정합니다. - 10 ~ 11자리의 숫자만 입력 가능합니다.")
	void changePhone_unsupportedFormat() throws Exception {
		// given
		Long customerId = 1L;
		ChangePhoneRequest request = new ChangePhoneRequest("");

		// when  // then
		mockMvc.perform(
				post("/api/v1/customer-phone/" + customerId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.message").isNotEmpty());
	}

	@Test
	@DisplayName(value = "API를 호출하여 계정을 삭제합니다.")
	void deleteAccount() throws Exception {
		// given
		Long customerId = 1L;

		// when  // then
		mockMvc.perform(
			delete("/api/v1/customer/" + customerId)
		)
			.andDo(print())
			.andExpect(status().isOk());
	}
}
