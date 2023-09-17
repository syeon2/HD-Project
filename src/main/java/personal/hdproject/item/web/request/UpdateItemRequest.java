package personal.hdproject.item.web.request;

import lombok.Builder;
import lombok.Getter;
import personal.hdproject.item.service.request.UpdateItemServiceRequest;

@Getter
public class UpdateItemRequest {

	private final String name;
	private final String description;
	private final Integer price;

	@Builder
	private UpdateItemRequest(String name, String description, Integer price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public UpdateItemServiceRequest toServiceRequest() {
		return UpdateItemServiceRequest.builder()
			.name(this.name)
			.description(this.description)
			.price(this.price)
			.build();
	}
}
