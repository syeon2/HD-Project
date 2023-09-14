package personal.hdproject.store.dao.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateStoreDto {

	private String name;
	private String phone;
	private String address;
	private Long storeCategoryId;

	@Builder
	private UpdateStoreDto(String name, String phone, String address, Long storeCategoryId) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategoryId = storeCategoryId;
	}
}
