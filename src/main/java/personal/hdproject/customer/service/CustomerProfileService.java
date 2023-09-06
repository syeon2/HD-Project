package personal.hdproject.customer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import personal.hdproject.customer.dao.CustomerRepository;
import personal.hdproject.customer.domain.Customer;
import personal.hdproject.customer.service.request.CreateCustomerServiceRequest;
import personal.hdproject.util.encryption.Sha256Util;
import personal.hdproject.util.error.exception.DuplicateEmailException;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {

	private final CustomerRepository customerRepository;

	public Long join(CreateCustomerServiceRequest request) {
		if (isEmailAlreadyRegistered(request.getEmail())) {
			throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
		}

		String encryptedPassword = getEncryptedPassword(request.getPassword());
		Customer savedCustomer = customerRepository.save(Customer.toEntity(request, encryptedPassword));

		return savedCustomer.getId();
	}

	public Long changeNickname(Long customerId, String nickname) {
		return customerRepository.updateNickname(customerId, nickname);
	}

	public Long changePhone(Long customerId, String phone) {
		return customerRepository.updatePhone(customerId, phone);
	}

	public void deleteAccount(Long customerId) {
		customerRepository.deleteById(customerId);
	}

	private String getEncryptedPassword(String password) {
		return Sha256Util.getEncrypt(password);
	}

	private boolean isEmailAlreadyRegistered(String email) {
		Optional<Customer> findCustomerOptional = customerRepository.findCustomerByEmail(email);

		return findCustomerOptional.isPresent();
	}
}
