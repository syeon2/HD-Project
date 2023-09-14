package personal.hdproject.store.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.store.service.request.CreateStoreServiceRequest;

@Getter
public class CreateStoreRequest {

	@NotBlank(message = "매장 이름은 필수 값입니다.")
	private String name;

	@NotBlank(message = "매장 전화번호는 필수 값입니다.")
	@Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11자리의 숫자만 입력 가능합니다.")
	private String phone;

	@NotBlank(message = "매장 주소는 필수 값입니다.")
	private String address;

	@NotNull(message = "매장 카테고리 아이디는 필수 값입니다.")
	private Long storeCategoryId;

	@NotNull(message = "회원 아이디는 필수 값입니다.")
	private Long memberId;

	@Builder
	private CreateStoreRequest(String name, String phone, String address, Long storeCategoryId, Long memberId) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategoryId = storeCategoryId;
		this.memberId = memberId;
	}

	public CreateStoreServiceRequest toServiceRequest() {
		return CreateStoreServiceRequest.builder()
			.name(this.name)
			.phone(this.phone)
			.address(this.address)
			.storeCategoryId(this.storeCategoryId)
			.memberId(this.memberId)
			.build();
	}
}
