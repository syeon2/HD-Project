package personal.hdproject.store.web.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.store.domain.StoreCategory;

@Getter
public class StoreCategoryResponse {

	private Long id;
	private String name;

	@Builder
	private StoreCategoryResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static StoreCategoryResponse toResponseDto(StoreCategory entity) {
		return StoreCategoryResponse.builder()
			.id(entity.getId())
			.name(entity.getName())
			.build();
	}
}
