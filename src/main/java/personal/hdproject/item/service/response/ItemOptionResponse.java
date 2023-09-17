package personal.hdproject.item.service.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.item.domain.ItemOption;

@Getter
public class ItemOptionResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final Long itemId;

	@Builder
	private ItemOptionResponse(Long id, String name, String description, Long itemId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.itemId = itemId;
	}

	public static ItemOptionResponse of(ItemOption itemOption) {
		return ItemOptionResponse.builder()
			.id(itemOption.getId())
			.name(itemOption.getName())
			.description(itemOption.getDescription())
			.build();
	}
}
