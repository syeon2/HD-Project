package personal.hdproject.customer.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.customer.dao.CustomerRepository;
import personal.hdproject.customer.domain.Customer;
import personal.hdproject.customer.service.request.CreateCustomerServiceRequest;
import personal.hdproject.util.encryption.Sha256Util;
import personal.hdproject.util.error.exception.DuplicateEmailException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CustomerProfileServiceTest {

	@Autowired
	private CustomerProfileService customerProfileService;

	@Autowired
	private CustomerRepository customerRepository;

	@AfterEach
	public void after() {
		customerRepository.deleteAll();
	}

	@Test
	@DisplayName(value = "이메일이 이미 존재하는지 검사하고, 비밀번호를 SHA-256으로 암호화하여 새로운 계정을 추가합니다.")
	void join() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		// when
		Long joinedCustomerId = customerProfileService.join(createCustomerDTO);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(joinedCustomerId);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer)
				.extracting("email", "nickname", "phone")
				.contains(email, nickname, phone),
			() -> fail("Customer should be present")
		);
	}

	@Test
	@DisplayName(value = "비밀번호는 암호화되어 저장합니다. - 기존의 비밀번호와 암호화된 비밀번호는 다릅니다.")
	void join_encryptedPassword() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		// when
		Long joinedCustomerId = customerProfileService.join(createCustomerDTO);

		// then
		String encryptedPassword = Sha256Util.getEncrypt(password);

		Optional<Customer> findCustomerOptional = customerRepository.findById(joinedCustomerId);

		assertThat(password).isNotEqualTo(encryptedPassword);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getPassword()).isEqualTo(encryptedPassword),
			() -> fail("Customer should be present")
		);
	}

	@Test
	@DisplayName(value = "이메일이 중복되어 예외가 발생합니다.")
	void join_duplicateEmail() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		Long joinCustomerId = customerProfileService.join(createCustomerDTO);

		Optional<Customer> findCustomerOptional = customerRepository.findById(joinCustomerId);
		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getEmail()).isEqualTo(email),
			() -> fail("Customer should be present")
		);

		// when // then
		assertThatThrownBy(() -> customerProfileService.join(createCustomerDTO))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	@DisplayName(value = "닉네임을 수정합니다.")
	void changeNickname() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		Long joinCustomerId = customerProfileService.join(createCustomerDTO);

		// when
		String changedNickname = "changed_nickname";
		customerProfileService.changeNickname(joinCustomerId, changedNickname);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(joinCustomerId);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getNickname()).isEqualTo(changedNickname),
			() -> fail("Customer should be present")
		);
	}

	@Test
	@DisplayName(value = "전화번호를 수정합니다.")
	void changePhone() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		Long joinCustomerId = customerProfileService.join(createCustomerDTO);

		// when
		String changedPhone = "99988887777";
		customerProfileService.changePhone(joinCustomerId, changedPhone);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(joinCustomerId);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getPhone()).isEqualTo(changedPhone),
			() -> fail("Customer should be present")
		);
	}

	@Test
	@DisplayName(value = "소비자 아이디를 삭제합니다.")
	void deleteAccount() {
		// given
		String email = "1234@gmail.com";
		String password = "12345678";
		String nickname = "nickname";
		String phone = "00011112222";

		CreateCustomerServiceRequest createCustomerDTO = getCreateCustomerServiceRequest(email, password, nickname,
			phone);

		Long joinCustomerId = customerProfileService.join(createCustomerDTO);

		// when
		customerProfileService.deleteAccount(joinCustomerId);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(joinCustomerId);

		assertThat(findCustomerOptional.isPresent()).isFalse();
	}

	private CreateCustomerServiceRequest getCreateCustomerServiceRequest(String email, String password, String nickname,
		String phone) {
		return CreateCustomerServiceRequest.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.phone(phone)
			.build();
	}
}
