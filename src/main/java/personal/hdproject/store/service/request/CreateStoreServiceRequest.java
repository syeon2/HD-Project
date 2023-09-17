package personal.hdproject.store.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateStoreServiceRequest {

	private final String name;
	private final String phone;
	private final String address;
	private final Long storeCategoryId;
	private final Long memberId;

	@Builder
	private CreateStoreServiceRequest(String name, String phone, String address, Long storeCategoryId, Long memberId) {
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.storeCategoryId = storeCategoryId;
		this.memberId = memberId;
	}
}
