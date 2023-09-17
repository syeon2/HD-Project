package personal.hdproject.item.web.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.item.service.request.CreateItemServiceRequest;

@Getter
public class CreateItemRequest {

	@NotBlank(message = "상품 이름은 필수 값입니다.")
	private final String name;

	@NotNull(message = "상품 설명은 null을 허용하지 않습니다.")
	private final String description;

	@Min(value = 0, message = "가격은 0이하 값을 허용하지 않습니다.")
	private final Integer price;

	@NotNull(message = "매장 아이디는 필수 값입니다.")
	private final Long storeId;

	@Builder
	private CreateItemRequest(String name, String description, Integer price, Long storeId) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.storeId = storeId;
	}

	public CreateItemServiceRequest toServiceRequest() {
		return CreateItemServiceRequest.builder()
			.name(this.name)
			.description(this.description)
			.price(this.price)
			.storeId(this.storeId)
			.build();
	}
}
