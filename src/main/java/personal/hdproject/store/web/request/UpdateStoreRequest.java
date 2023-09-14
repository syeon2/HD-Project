package personal.hdproject.store.web.request;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.store.service.request.UpdateStoreServiceRequest;

@Getter
public class UpdateStoreRequest {

	private final String name;
	private final String phone;
	private final String address;
	private final Long storeCategoryId;

	@Builder
	private UpdateStoreRequest(String name, String phone, String address, Long storeCategoryId) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategoryId = storeCategoryId;
	}

	public UpdateStoreServiceRequest toServiceRequest() {
		return UpdateStoreServiceRequest.builder()
			.name(this.name)
			.phone(this.phone)
			.address(this.address)
			.storeCategoryId(this.storeCategoryId)
			.build();
	}
}
