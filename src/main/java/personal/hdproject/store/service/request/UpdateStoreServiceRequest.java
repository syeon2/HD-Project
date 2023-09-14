package personal.hdproject.store.service.request;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.store.dao.common.UpdateStoreDto;

@Getter
public class UpdateStoreServiceRequest {

	private final String name;
	private final String phone;
	private final String address;
	private final Long storeCategoryId;

	@Builder
	private UpdateStoreServiceRequest(String name, String phone, String address, Long storeCategoryId) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategoryId = storeCategoryId;
	}

	public UpdateStoreDto toUpdateDto() {
		return UpdateStoreDto.builder()
			.name(this.name)
			.phone(this.phone)
			.address(this.address)
			.storeCategoryId(this.storeCategoryId)
			.build();
	}
}
