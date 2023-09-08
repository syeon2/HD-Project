package personal.hdproject.customer.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.customer.domain.Customer;
import personal.hdproject.customer.domain.Grade;

@Getter
public class CustomerInfoResponse {

	private final Long id;
	private final String email;
	private final String nickname;
	private final String phone;
	private final Grade grade;

	@Builder
	private CustomerInfoResponse(Long id, String email, String nickname, String phone, Grade grade) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.phone = phone;
		this.grade = grade;
	}

	public static CustomerInfoResponse toCustomerResponse(Customer customer) {
		return CustomerInfoResponse.builder()
			.id(customer.getId())
			.email(customer.getEmail())
			.nickname(customer.getNickname())
			.phone(customer.getPhone())
			.grade(customer.getGrade())
			.build();
	}
}
