package personal.hdproject.item.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.item.domain.Item;

@Getter
public class ItemResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final Integer price;
	private final Long storeId;

	@Builder
	private ItemResponse(Long id, String name, String description, Integer price, Long storeId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.storeId = storeId;
	}

	public static ItemResponse toResponseDto(Item item) {
		return ItemResponse.builder()
			.id(item.getId())
			.name(item.getName())
			.description(item.getDescription())
			.price(item.getPrice())
			.storeId(item.getStore().getId())
			.build();
	}
}
