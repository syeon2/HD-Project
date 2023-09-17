package personal.hdproject.store.web.response;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.store.domain.Store;

@Getter
public class StoreResponse {

	private final Long id;
	private final String name;
	private final String phone;
	private final String address;

	@Builder
	private StoreResponse(Long id, String name, String phone, String address) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	public static StoreResponse toResponseDto(Store entity) {
		return StoreResponse.builder()
			.id(entity.getId())
			.name(entity.getName())
			.phone(entity.getPhone())
			.address(entity.getAddress())
			.build();
	}
}
