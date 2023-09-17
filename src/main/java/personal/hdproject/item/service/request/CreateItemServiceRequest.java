package personal.hdproject.item.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateItemServiceRequest {

	private final String name;
	private final String description;
	private final Integer price;
	private final Long storeId;

	@Builder
	private CreateItemServiceRequest(String name, String description, Integer price, Long storeId) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.storeId = storeId;
	}
}
