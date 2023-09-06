package personal.hdproject.customer.dao.jpa;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import personal.hdproject.customer.dao.CustomerRepository;
import personal.hdproject.customer.domain.Customer;
import personal.hdproject.customer.domain.Grade;

@SpringBootTest
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	@DisplayName(value = "소비자의 닉네임을 수정합니다.")
	void updateNickname() {
		// given
		String nickname = "nickname1";

		Customer customer = createCustomer("1235@gmail.com", "12345678", nickname, "00011112222");
		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer.getNickname()).isEqualTo(nickname);

		// when
		Long customerId = savedCustomer.getId();

		String fixedNickname = "fixNickname";
		customerRepository.updateNickname(customerId, fixedNickname);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(customerId);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getNickname()).isEqualTo(fixedNickname),
			() -> fail("Customer should be present")
		);
	}

	@Test
	@DisplayName(value = "소비자의 전화번호를 수정합니다.")
	void updatePhone() {
		// given
		String phone = "00011112222";

		Customer customer = createCustomer("1234@gmail.com", "1234", "nickname", phone);
		Customer savedCustomer = customerRepository.save(customer);

		assertThat(savedCustomer.getPhone()).isEqualTo(phone);

		// when
		Long customerId = savedCustomer.getId();

		String fixPhone = "22233334444";
		customerRepository.updatePhone(customerId, fixPhone);

		// then
		Optional<Customer> findCustomerOptional = customerRepository.findById(customerId);

		findCustomerOptional.ifPresentOrElse(
			findCustomer -> assertThat(findCustomer.getPhone()).isEqualTo(fixPhone),
			() -> fail("Customer should be present")
		);
	}

	private Customer createCustomer(String mail, String password, String nickname1, String phone) {
		return Customer.builder()
			.email(mail)
			.password(password)
			.nickname(nickname1)
			.phone(phone)
			.grade(Grade.BASIC)
			.build();
	}
}
