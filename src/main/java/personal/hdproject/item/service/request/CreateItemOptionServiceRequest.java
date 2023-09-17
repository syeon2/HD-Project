package personal.hdproject.item.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateItemOptionServiceRequest {

	private final String name;
	private final String description;
	private final Integer price;
	private final Long itemId;

	@Builder
	private CreateItemOptionServiceRequest(String name, String description, Integer price, Long itemId) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.itemId = itemId;
	}
}
